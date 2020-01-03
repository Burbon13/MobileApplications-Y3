import React from 'react';
import {createAppContainer, createSwitchNavigator} from 'react-navigation';
import {getLogger, navigation} from './src/core';
import {AuthLoading, Auth, AuthStore} from './src/auth';

const log = getLogger('App');

const AppContainer = createAppContainer(
  createSwitchNavigator(
    {AuthLoading, Auth},
    {initialRouteName: 'AuthLoading'},
  ),
);

const App = () => {
  log('Rendering');
  return (
    <AuthStore>
      <AppContainer ref={navigatorRef => {
        navigation.setTopLevelNavigator(navigatorRef);
      }}/>
    </AuthStore>
  );
};

export default App;
