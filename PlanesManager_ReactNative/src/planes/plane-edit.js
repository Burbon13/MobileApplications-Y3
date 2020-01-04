import React from 'react';
import {View, TextInput, Button} from 'react-native';

import {getLogger} from '../core';
import {PlaneContext} from "./plane-context";

const log = getLogger('PlaneEdit');

export const PlaneEdit = ({text = '', navigation}) => {
  const [value, onChangeText] = React.useState(text);
  return (
    <PlaneContext.Consumer>
      {({onSubmit}) => (
        <View>
          <TextInput
            style={{height: 40, borderColor: 'gray', borderWidth: 1}}
            onChangeText={text => onChangeText(text)}
            value={value}
          />
          <Button
            title="Submit"
            onPress={() => {
              onSubmit(value)
                .then(() => navigation.goBack())
            }}
          />
        </View>
      )}
    </PlaneContext.Consumer>
  );
};

PlaneEdit.navigationOptions = () => ({
  headerTitle: 'Plane Edit',
});
