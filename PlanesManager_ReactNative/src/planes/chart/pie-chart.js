import React, {Component} from 'react';
import {StyleSheet, View} from 'react-native';
import Pie from 'react-native-pie';
import {PlaneContext} from '../plane-context';
import {getLogger} from '../../core';
import {Text} from 'react-native';

const log = getLogger('pie-chart');

const _generatePieChartData = brandsCount => [
  {percentage: Math.ceil((100 * brandsCount.AIRBUS) / brandsCount.total), color: '#f00'},
  {percentage: Math.ceil((100 * brandsCount.BOEING) / brandsCount.total), color: '#0f0'},
  {percentage: Math.ceil((100 * brandsCount.ATR) / brandsCount.total), color: '#00f'},
  {percentage: Math.ceil((100 * brandsCount.BOMBARDIER) / brandsCount.total), color: '#ff0'},
  {percentage: Math.ceil((100 * brandsCount.EMBRAER) / brandsCount.total), color: '#ff3ede'},
];

const PieCharComponent = ({brand, color}) => (
  <View style={{flexDirection: 'row', alignItems: 'center'}}>
    <View style={{backgroundColor: color, width: 10, height: 10, marginRight: 5}}/>
    <Text>{brand}</Text>
  </View>);

export class BrandsPieChart extends Component {
  render() {
    log('Rendering');
    return (
      <View style={_styles.container}>
        <View>
          <PieCharComponent brand="AIRBUS" color="#f00"/>
          <PieCharComponent brand="BOEING" color="#0f0"/>
          <PieCharComponent brand="ATR" color="#00f"/>
          <PieCharComponent brand="BOMBARDIER" color="#ff0"/>
          <PieCharComponent brand="EMBRAER" color="#ff3ede"/>
        </View>
        <PlaneContext.Consumer>
          {({brandsCount}) => {
            log(`Showing brands count pie chart for data: ${JSON.stringify(brandsCount, null, 2)}`);
            return (
              <Pie
                radius={180}
                innerRadius={70}
                sections={_generatePieChartData(brandsCount)}
              />
            );
          }}
        </PlaneContext.Consumer>
      </View>
    );
  }
}

BrandsPieChart.navigationOptions = {
  headerTitle: 'Planes brand count',
};

const _styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'space-around',
  },
  gauge: {
    position: 'absolute',
    width: 100,
    height: 100,
    alignItems: 'center',
    justifyContent: 'center',
  },
  gaugeText: {
    backgroundColor: 'transparent',
    color: '#000',
    fontSize: 24,
  },
});
