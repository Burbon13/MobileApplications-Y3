import React, {useCallback, useContext, useEffect} from 'react';
import {PlaneContext} from './plane-context';
import {getLogger, httpGet, httpPost, httpPut, httpDelete} from '../core';
import {AuthContext} from '../auth/context';
import {AsyncStorage} from 'react-native';

const log = getLogger('PlanesStore');

const initialState = {
  isLoading: false,
  planes: null,
  brandsCount: null,
  loadingError: null,
};

export const PlanesStore = ({children}) => {
  const [state, setState] = React.useState(initialState);
  const {isLoading, planes, loadingError, brandsCount} = state;
  const {token} = useContext(AuthContext);

  useEffect(() => {
    if (token && !planes && !loadingError && !isLoading) {
      setState({isLoading: true, loadingError: null});

      AsyncStorage.getItem('planes-manager:planes', (err, result) => {
        log(`Planes retrieved from async storage, setting state: ${result}`);
        if (result !== null) {
          const planes = JSON.parse(result);
          setState({isLoading: false, planes: planes, brandsCount: _getBrandCounts(planes)});
        }
        httpGet('api/plane')
          .then(json => {
            log('Loading planes succeeded, saving async storage');
            AsyncStorage
              .setItem('planes-manager:planes', JSON.stringify(json))
              .catch(error => log(`Saving to async storage failed: ${error}`));
            setState({isLoading: false, planes: json, brandsCount: _getBrandCounts(json)});
          })
          .catch(loadingError => {
            log(`Loading planes failed: ${loadingError}`);
          });
      });
    }
  });

  const onSubmit = useCallback(async (plane) => {
    log('POST plane started');
    return httpPost('api/plane', plane)
      .then(json => {
        log('POST plane succeeded');
        const newPlanes = planes.concat(json);
        newPlanes.sort((plane1, plane2) => plane1.tailNumber.localeCompare(plane2.tailNumber));
        setState({planes: newPlanes, brandsCount: brandsCount});
        return AsyncStorage.setItem('planes-manager:planes', JSON.stringify(newPlanes));
      })
      .then(result => {
        log('Saved new plane to async storage');
        return Promise.resolve(result);
      })
      .catch(error => {
        log(`POST plane failed: ${error}`);
        return Promise.reject(error);
      });
  }, [state]);

  const onGeolocation = useCallback(async (tailNumber) => {
    log('GET plane geolocation started');
    return httpGet(`api/plane/position/${tailNumber}`)
      .then(json => {
        log('GET plane geolocation succeeded');
        return Promise.resolve(json);
      })
      .catch(error => {
        log(`GET plane geolocation failed: ${error}`);
        return Promise.reject(error);
      });
  }, []);

  const onUpdate = useCallback(async (updatedPlane) => {
    log('POST plane started');
    return httpPut('api/plane', updatedPlane)
      .then(json => {
        let planeIndex = planes.findIndex(plane => updatedPlane.tailNumber === plane.tailNumber);
        if (planeIndex === -1) {
          throw new Error(`Error finding plane with tailNumber=${updatedPlane.tailNumber}`);
        }
        planes[planeIndex] = updatedPlane;
        setState({planes: planes, brandsCount: brandsCount});
        return AsyncStorage.setItem('planes-manager:planes', JSON.stringify(planes));
      })
      .then(result => {
        log('Plane updated successfully stored to async storage');
        return Promise.resolve(result);
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
        setState({planes: planes, brandsCount: brandsCount});
        return AsyncStorage.setItem('planes-manager:planes', JSON.stringify(planes));
      })
      .then(result => Promise.resolve(result))
      .catch(error => {
        log(`DELETE plane failed: ${error}`);
        return Promise.reject(error);
      });
  }, [state]);

  log(`Rendering, isLoading=${isLoading}`);
  const value = {...state, onSubmit, onUpdate, onDelete, onGeolocation};
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
