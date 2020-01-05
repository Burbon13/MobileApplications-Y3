import React, {Component} from 'react'
import {StyleSheet, View,} from 'react-native'
import Pie from 'react-native-pie';
import {PlaneContext} from "../plane-context";
import {getLogger} from "../../core";
import {Text} from "react-native";

const log = getLogger('pie-chart');

export class BrandsPieChart extends Component {
  render() {
    return (
      <View style={_styles.container}>
        <View>
          <View style={{flexDirection: 'row', alignItems: 'center'}}>
            <View style={{backgroundColor: '#f00', width: 10, height: 10, marginRight: 5}}/>
            <Text>AIRBUS</Text>
          </View>
          <View style={{flexDirection: 'row', alignItems: 'center'}}>
            <View style={{backgroundColor: '#0f0', width: 10, height: 10, marginRight: 5}}/>
            <Text>BOEING</Text>
          </View>
          <View style={{flexDirection: 'row', alignItems: 'center'}}>
            <View style={{backgroundColor: '#00f', width: 10, height: 10, marginRight: 5}}/>
            <Text>ATR</Text>
          </View>
          <View style={{flexDirection: 'row', alignItems: 'center'}}>
            <View style={{backgroundColor: '#ff3ede', width: 10, height: 10, marginRight: 5}}/>
            <Text>EMBRAER</Text>
          </View>
          <View style={{flexDirection: 'row', alignItems: 'center'}}>
            <View style={{backgroundColor: '#ff0', width: 10, height: 10, marginRight: 5}}/>
            <Text>BOMBARDIER</Text>
          </View>
        </View>
        <PlaneContext.Consumer>
          {({brandsCount}) => {
            log(`Showing brands count pie chart for data: ${JSON.stringify(brandsCount, null, 2)}`);
            return (
              <Pie
                radius={180}
                innerRadius={70}
                sections={[
                  {
                    percentage: Math.ceil((100 * brandsCount.AIRBUS) / brandsCount.total),
                    color: '#f00',
                  },
                  {
                    percentage: Math.ceil((100 * brandsCount.BOEING) / brandsCount.total),
                    color: '#0f0',
                  },
                  {
                    percentage: Math.ceil((100 * brandsCount.ATR) / brandsCount.total),
                    color: '#00f',
                  },
                  {
                    percentage: Math.ceil((100 * brandsCount.BOMBARDIER) / brandsCount.total),
                    color: '#ff0',
                  },
                  {
                    percentage: Math.ceil((100 * brandsCount.EMBRAER) / brandsCount.total),
                    color: '#ff3ede',
                  },
                ]}
              />
            )
          }}
        </PlaneContext.Consumer>
      </View>
    )
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
