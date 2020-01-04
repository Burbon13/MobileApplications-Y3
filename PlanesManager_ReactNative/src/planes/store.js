import React, {useCallback, useContext, useEffect} from 'react';
import {PlaneContext} from './plane-context';
import {getLogger, httpGet, httpPost} from '../core';
import {AuthContext} from '../auth/context';

const log = getLogger('PlanesStore');

const initialState = {
  isLoading: false,
  planes: null,
  loadingError: null,
};

export const PlanesStore = ({children}) => {
  const [state, setState] = React.useState(initialState);
  const {isLoading, planes, loadingError} = state;
  const {token} = useContext(AuthContext);

  useEffect(() => {
    if (token && !planes && !loadingError && !isLoading) {
      log('Loading planes started');
      setState({isLoading: true, loadingError: null});
      httpGet('api/plane')
        .then(json => {
          log('Loading planes succeeded');
          setState({isLoading: false, planes: json});
        })
        .catch(loadingError => {
          log('Loading planes failed');
          setState({isLoading: false, loadingError})
        });
    }
  }, [token]);

  const onSubmit = useCallback(async (text) => {
    log('POST plane started');
    return httpPost('api/plane', {text})
      .then(json => {
        log('POST plane succeeded');
        setState({planes: planes.concat(json)});
        return Promise.resolve(json);
      })
      .catch(error => {
        log('POST plane failed');
        return Promise.reject(error);
      });
  }, []);

  log('render', isLoading);
  const value = {...state, onSubmit};
  return (
    <PlaneContext.Provider value={value}>
      {children}
    </PlaneContext.Provider>
  );
};
