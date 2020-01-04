import React, {useContext, useEffect} from 'react';
import {ActivityIndicator, View} from 'react-native';
import {getLogger} from '../core';
import {AuthContext} from './context';

const log = getLogger('AuthLoading');

export const AuthLoading = ({navigation}) => {
  const {onLoadToken} = useContext(AuthContext);

  useEffect(() => {
    log('onLoadToken...');
    onLoadToken()
      .then(token => {
        log('onLoadToken succeeded');
        navigation.navigate(token ? 'Planes' : 'Auth');
      });
  }, []);

  log('Rendering AuthLoading');
  return (
    <View>
      <ActivityIndicator/>
    </View>
  );
};
