import React, {useCallback, useReducer} from 'react';
import {AsyncStorage} from 'react-native';
import {getLogger, httpPost, setToken} from '../core';
import {Provider} from './context';


const log = getLogger('(auth) AuthStore');

const TOKEN_KEY = 'TOKEN';
const SET_TOKEN = 'SET_TOKEN';
const LOGIN_STARTER = 'LOGIN_STARTED';
const LOGIN_SUCCEEDED = 'LOGIN_SUCCEEDED';
const LOGIN_FAILED = 'LOGIN_FAILED';

const initialState = {
  token: null,
  loginError: null,
  loginInProgress: null,
};

const reducer = (state, action) => {
  log('reducer', action);
  const {type, payload} = action;
  switch (type) {
    case SET_TOKEN:
      return {...state, token: payload.token};
    case LOGIN_STARTER:
      return {...state, signInError: null, signInInProgress: true};
    case LOGIN_SUCCEEDED:
      return {...state, token: payload.token, signInInProgress: false};
    case LOGIN_FAILED:
      return {...state, signInError: payload.error, signInInProgress: false};
    default:
      return state;
  }
};

export const AuthStore = ({children}) => {
  const [state, dispatch] = useReducer(reducer, initialState);

  const onLoadToken = useCallback(async () => {
    log('AuthLoading token');
    let token = null;
    try {
      const value = await AsyncStorage.getItem(TOKEN_KEY);
      token = value || null;
      log('Token loaded successfully', token, value);
    } catch (error) {
      log('Exception occurred while loading token', error);
    }
    setToken(token);
    dispatch({type: SET_TOKEN, payload: {token}});
    return Promise.resolve(token);
  }, []);

  const onLogin = useCallback(async (username, password) => {
    log('On Login');
    dispatch({type: LOGIN_STARTER});
    return httpPost('api/auth/login', {username, password})
      .then(tokenHolder => {
        log('Login successful');
        const {token} = tokenHolder;
        setToken(token);
        dispatch({type: LOGIN_SUCCEEDED, payload: {token}});
        AsyncStorage.setItem(TOKEN_KEY, token)
          .catch(error => {
            log('Error while saving token', error);
          });
        return token;
      })
      .catch(error => {
        dispatch({type: LOGIN_FAILED, payload: {error}});
        return Promise.reject(error);
      });
  }, []);

  const onLogout = useCallback(async () => {
    log('On Logout');
    try {
      await AsyncStorage.removeItem(TOKEN_KEY);
      log('Successfully removed TOKEN');
    } catch (error) {
      log('Error occurred while removing token', error);
    }
    setToken(null);
    dispatch({type: SET_TOKEN, payload: {token: null}});
    return Promise.resolve(null);
  }, []);

  const value = {...state, onLoadToken, onLogin, onLogout};

  log('Rendering', value);
  return (
    <Provider value={value}>
      {children}
    </Provider>
  );
};
