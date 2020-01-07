import React, {useContext, useEffect} from 'react';
import {ActivityIndicator, View} from 'react-native';
import {AuthContext} from './context';
import {getLogger, navigation} from '../core';

const log = getLogger('AuthLoading');

export const AuthLoading = () => {
  log('Rendering');

  const {onLoadToken} = useContext(AuthContext);

  useEffect(() => {
    log('useEffect(): onLoadToken...');
    onLoadToken()
      .then(token => {
        log(`onLoadToken succeeded, token="${token}"`);
        navigation.navigate(token ? 'Planes' : 'Auth');
      })
      .catch(error => {
        log(`Error occurred while loading token: ${error}`);
      });
  }, []);

  return (
    <View>
      <ActivityIndicator/>
    </View>
  );
};
