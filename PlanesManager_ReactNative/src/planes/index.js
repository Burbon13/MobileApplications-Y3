import React from 'react';
import {createStackNavigator} from "react-navigation-stack";
import {PlaneEdit} from './form/plane-edit';
import {PlaneList} from './plane-list';

export const Planes = createStackNavigator({
  PlanesList: {screen: PlaneList},
  PlaneEdit: {screen: PlaneEdit},
});

export * from './store';
