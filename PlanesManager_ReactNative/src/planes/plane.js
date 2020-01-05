import React from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import {getLogger, navigation} from '../core';

const log = getLogger('Item');

export default ({plane}) => {
  plane = plane.item;
  return (
    <TouchableOpacity
      onPress={() => {
        navigation.navigate('PlaneEdit', {plane: plane});
      }
      }>
      <View style={_styles.container}>
        <View>
          <View style={{flexDirection: 'row', marginBottom: 10}}>
            <Text style={_styles.brandText}>{plane.brand}</Text>
            <Text style={_styles.modelText}>{plane.model}</Text>
          </View>
          <View style={{flexDirection: 'row'}}>
            <Text style={_styles.priceText}>{_prettierPrice(plane.price)}</Text>
            <Text style={_styles.yearText}>{plane.fabricationYear}</Text>
          </View>
        </View>
        <Text style={_styles.tailNumberText}>{plane.tailNumber}</Text>
      </View>
      <View
        style={{
          borderBottomColor: 'black',
          borderBottomWidth: 1,
        }}
      />
    </TouchableOpacity>
  );
};

const _prettierPrice = price => {
  const intPrice = parseInt(price);
  if (intPrice >= 1000000000000) {
    return `US$${Math.floor(intPrice / 1000000000000)} trillion`;
  }
  if (intPrice >= 1000000000) {
    return `US$${Math.floor(intPrice / 1000000000)} million`;
  }
  if (intPrice >= 1000000) {
    return `US$${Math.floor(intPrice / 1000000)} million`;
  }
  if (intPrice >= 1000) {
    return `US$${Math.floor(intPrice / 1000)} K`;
  }
  return `US$${price}`
};

const _styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-between',
    padding: 10,
    flexDirection: 'row',
  },
  brandText: {
    marginRight: 8,
    fontSize: 30,
    textAlignVertical: 'bottom',
  },
  modelText: {
    fontSize: 15,
    textAlignVertical: 'bottom',
    paddingBottom: 4,
  },
  priceText: {
    marginRight: 8,
    fontSize: 15,
  },
  yearText: {
    fontSize: 15,
  },
  tailNumberText: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlignVertical: 'center',
  },
});
