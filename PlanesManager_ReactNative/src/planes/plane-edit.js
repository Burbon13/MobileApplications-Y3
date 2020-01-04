import React from 'react';
import {View, TextInput, Picker, StyleSheet, TouchableOpacity, Text} from 'react-native';
import RNPickerSelect from 'react-native-picker-select'
import {getLogger} from '../core';
import {PlaneContext} from "./plane-context";

const log = getLogger('PlaneEdit');

export const PlaneEdit = ({plane = {}, navigation}) => {
  const [tailNumber, onChangeTailNumber] = React.useState(plane.tailNumber);
  const [model, onChangeModel] = React.useState(plane.model);
  const [year, onChangeYear] = React.useState(plane.fabricationYear);
  const [price, onChangePrice] = React.useState(plane.price);

  return (
    <PlaneContext.Consumer>
      {({onSubmit}) => (
        <View style={_styles.container}>
          <TextInput
            style={[_styles.formElemMargin, _styles.textInput]}
            placeholder='Tail Number'
            onChangeText={text => onChangeTailNumber(text)}
            value={tailNumber}
          />
          <View
            style={_styles.formElemMargin}>
            <RNPickerSelect
              placeholder={{label: 'Select the plane\'s brand'}}
              onValueChange={(value) => console.log(value)}
              items={[
                { label: 'Boeing', value: 'BOEING' },
                { label: 'Airbus', value: 'AIRBUS' },
                { label: 'ATR', value: 'ATR' },
                { label: 'Embraer', value: 'EMBRAER' },
                { label: 'Bombardier', value: 'BOMBARDIER' },
              ]}
            />
          </View>
          <TextInput
            style={[_styles.formElemMargin, _styles.textInput]}
            placeholder="Model"
            onChangeText={text => onChangeModel(text)}
            secureTextEntry={true}
            value={model}
          />
          <TextInput
            style={[_styles.formElemMargin, _styles.textInput]}
            placeholder="Fabrication Year"
            onChangeText={text => onChangeYear(text)}
            secureTextEntry={true}
            value={year}
          />
          <View
            style={_styles.formElemMargin}>
            <RNPickerSelect
              placeholder={{label: 'Select the plane\'s engine type'}}
              onValueChange={(value) => console.log(value)}
              items={[
                { label: 'Turboprop', value: 'TURBOPROP' },
                { label: 'Ramjet', value: 'RAMJET' },
                { label: 'Turbojet', value: 'TURBOJET' },
                { label: 'Turbofan', value: 'TURBOFAN' },
              ]}
            />
          </View>
          <TextInput
            style={[_styles.formElemMargin, _styles.textInput]}
            placeholder="Price"
            onChangeText={text => onChangePrice(text)}
            secureTextEntry={true}
            value={price}
          />
          <TouchableOpacity
            style={[_styles.formElemMargin, _styles.button]}
            onPress={() => {
              log('Sign in button pressed');
              onSubmit(value)
                .then(() => navigation.goBack())
                .catch(error => {
                  log(`Add plane error ${error}`);
                });
            }}
          >
            <Text>Add plane</Text>
          </TouchableOpacity>
        </View>
      )}
    </PlaneContext.Consumer>
  );
};

PlaneEdit.navigationOptions = () => ({
  headerTitle: 'Plane Edit',
});

const _styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    paddingHorizontal: 10
  },
  formElemMargin: {
    padding: 10,
    marginHorizontal: 30,
    marginVertical: 10,
  },
  textInput: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
  },
  errorText: {
    color: 'red',
    textAlign: 'center',
    fontSize: 24,
    fontWeight: 'bold',
  },
  button: {
    alignItems: 'center',
    backgroundColor: '#DDDDDD',
  }
});
