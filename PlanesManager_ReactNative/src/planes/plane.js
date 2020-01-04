import React from 'react';
import {Text, View} from 'react-native';

import {getLogger} from '../core';

const log = getLogger('Item');

export default ({plane}) => {
  plane = plane.item;
  return (
    <View>
      <Text>{plane.tailNumber}</Text>
      <Text>{plane.brand}</Text>
      <Text>{plane.model}</Text>
      <Text>{plane.price}</Text>
      <Text>{plane.fabricationYear}</Text>
      <Text>{plane.engine}</Text>
      <View
        style={{
          borderBottomColor: 'black',
          borderBottomWidth: 1,
        }}
      />
    </View>
  );
};
