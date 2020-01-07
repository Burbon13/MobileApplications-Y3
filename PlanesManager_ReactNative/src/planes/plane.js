import React from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import {getLogger, navigation} from '../core';

const log = getLogger('Item');

const _brandColor = {
  BOEING: '#0039a6',
  AIRBUS: '#eb34eb',
  ATR: '#1ac7b6',
  BOMBARDIER: '#23ba35',
  EMBRAER: '#e6e21c',
};

export default ({plane}) => {
  // log('Rendering');
  return (
    <TouchableOpacity onPress={() => {
      navigation.navigate('PlaneEdit', {plane: plane});
    }}>
      <View style={_styles.container}>
        <View>
          <View style={_styles.firstLeftPane}>
            <Text style={{..._styles.brandText, color: _brandColor[plane.brand]}}>{plane.brand}</Text>
            <Text style={_styles.modelText}>{plane.model}</Text>
          </View>
          <View style={_styles.secondLeftPane}>
            <Text
              style={{..._styles.priceText, color: _getPriceColor(plane.price)}}>{_prettierPrice(plane.price)}</Text>
            <Text style={_styles.yearText}>{plane.fabricationYear}</Text>
          </View>
        </View>
        <Text style={_styles.tailNumberText}>{plane.tailNumber}</Text>
      </View>
      <View style={_styles.horizontalLine}/>
    </TouchableOpacity>
  );
};

const _getPriceColor = price => {
  const intPrice = parseInt(price);
  if (intPrice >= 1000000) {
    return '#00e81f';
  }
  if (intPrice >= 1000) {
    return '#489c55';
  }
  return '#466b4b';
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
  return `US$${price}`;
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
  horizontalLine: {
    borderBottomColor: 'black',
    borderBottomWidth: 1,
  },
  firstLeftPane: {
    flexDirection: 'row',
    marginBottom: 10,
  },
  secondLeftPane: {
    flexDirection: 'row',
  },
});
