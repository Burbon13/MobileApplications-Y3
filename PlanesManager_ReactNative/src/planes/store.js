import React, {useCallback, useContext, useEffect} from 'react';
import {PlaneContext} from './plane-context';
import {getLogger, httpGet, httpPost, httpPut, httpDelete} from '../core';
import {AuthContext} from '../auth/context';

const log = getLogger('PlanesStore');

const initialState = {
  isLoading: false,
  planes: null,
  brandsCount: null,
  loadingError: null,
};

export const PlanesStore = ({children}) => {
  const [state, setState] = React.useState(initialState);
  const {isLoading, planes, loadingError} = state;
  const {token} = useContext(AuthContext);

  useEffect(() => {
    if (token && !planes && !loadingError && !isLoading) {
      setState({isLoading: true, loadingError: null});
      httpGet('api/plane')
        .then(json => {
          log('Loading planes succeeded');
          setState({isLoading: false, planes: json, brandsCount: _getBrandCounts(json)});
        })
        .catch(loadingError => {
          log('Loading planes failed');
          setState({isLoading: false, loadingError});
        });
    }
  });

  const onSubmit = useCallback(async (plane) => {
    log('POST plane started');
    return httpPost('api/plane', plane)
      .then(json => {
        log('POST plane succeeded');
        setState({planes: planes.concat(json)});
        return Promise.resolve(json);
      })
      .catch(error => {
        log(`POST plane failed: ${error}`);
        return Promise.reject(error);
      });
  }, [state]);

  const onUpdate = useCallback(async (updatedPlane) => {
    log('POST plane started');
    return httpPut('api/plane', updatedPlane)
      .then(json => {
        let planeIndex = planes.findIndex(plane => updatedPlane.tailNumber === plane.tailNumber);
        if (planeIndex === -1) {
          throw new Error(`Error finding plane with tailNumber=${updatedPlane.tailNumber}`);
        }
        planes[planeIndex] = updatedPlane;
        setState({planes: planes});
        return Promise.resolve(json);
      })
      .catch(error => {
        log(`POST plane failed: ${error}`);
        return Promise.reject(error);
      });
  }, [state]);

  const onDelete = useCallback(async (tailNumber) => {
    log('DELETE plane started');
    return httpDelete(`api/plane/${tailNumber}`)
      .then(json => {
        log(JSON.stringify(json, null, 2));
        let planeIndex = planes.findIndex(plane => tailNumber === plane.tailNumber);
        if (planeIndex === -1) {
          throw new Error(`Error finding plane with tailNumber=${tailNumber}`);
        }
        planes.splice(planeIndex, 1);
        setState({planes: planes});
        return Promise.resolve(json);
      })
      .catch(error => {
        log(`DELETE plane failed: ${error}`);
        return Promise.reject(error);
      });
  }, [state]);

  log(`Rendering, isLoading=${isLoading}`);
  const value = {...state, onSubmit, onUpdate, onDelete};
  return (
    <PlaneContext.Provider value={value}>
      {children}
    </PlaneContext.Provider>
  );
};

const _getBrandCounts = (planes) => {
  let constBrands = planes.reduce((countMap, plane) => {
    const brand = plane.brand;
    if (!countMap.hasOwnProperty(brand)) {
      countMap[brand] = 0;
    }
    countMap[brand]++;
    return countMap;
  }, {});
  constBrands.total = planes.length;
  return constBrands;
};
