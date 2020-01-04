import React from 'react';
import {Text, View} from 'react-native';

import {getLogger} from '../core';

const log = getLogger('Item');

export default ({plane = {}}) => {
  log('render');
  return (
    <View>
      <Text>{plane.tailNumber}</Text>
      <Text>{plane.brand}</Text>
      <Text>{plane.model}</Text>
      <Text>{plane.price}</Text>
    </View>
  );
};
