import React from 'react';
import {createStackNavigator} from "react-navigation-stack";
import {PlaneEdit} from './plane-edit';
import {PlaneList} from './plane-list';

export const Planes = createStackNavigator({
  PlanesLis: {screen: PlaneList},
  PlaneEdit: {screen: PlaneEdit},
});

export * from './store';
