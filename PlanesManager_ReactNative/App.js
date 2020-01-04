import React from 'react';
import {createAppContainer, createSwitchNavigator} from 'react-navigation';
import {getLogger, navigation} from './src/core';
import {AuthLoading, Auth, AuthStore} from './src/auth';
import {Planes, PlanesStore} from "./src/planes";

const log = getLogger('App');

const AppContainer = createAppContainer(
  createSwitchNavigator(
    {
      AuthLoading: AuthLoading,
      Auth: Auth,
      Planes: Planes
    },
    {initialRouteName: 'AuthLoading'},
  ),
);

const App = () => {
  log('Rendering');
  return (
    <AuthStore>
      <PlanesStore>
        <AppContainer ref={navigatorRef => {
          navigation.setTopLevelNavigator(navigatorRef);
        }}/>
      </PlanesStore>
    </AuthStore>
  );
};

export default App;
