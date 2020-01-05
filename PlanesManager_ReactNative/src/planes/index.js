import React from 'react';
import {createStackNavigator} from "react-navigation-stack";
import {PlaneEdit} from './form/plane-edit';
import {BrandsPieChart} from './chart/pie-chart';
import {PlaneList} from './plane-list';

export const Planes = createStackNavigator({
  PlanesList: {screen: PlaneList},
  PlaneEdit: {screen: PlaneEdit},
  BrandsPieChart: {screen: BrandsPieChart}
});

export * from './store';
