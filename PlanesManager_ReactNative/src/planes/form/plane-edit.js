import React from 'react';
import {View, TextInput, Picker, StyleSheet, TouchableOpacity, Text, ScrollView} from 'react-native';
import RNPickerSelect from 'react-native-picker-select';
import {getLogger, navigation} from '../../core';
import {PlaneContext} from '../plane-context';
import {validationService} from './service';

const log = getLogger('PlaneEdit');

const _brandsLabels = [
  {label: 'Boeing', value: 'BOEING'},
  {label: 'Airbus', value: 'AIRBUS'},
  {label: 'ATR', value: 'ATR'},
  {label: 'Embraer', value: 'EMBRAER'},
  {label: 'Bombardier', value: 'BOMBARDIER'},
];

const _engineLabels = [
  {label: 'Turboprop', value: 'TURBOPROP'},
  {label: 'Ramjet', value: 'RAMJET'},
  {label: 'Turbojet', value: 'TURBOJET'},
  {label: 'Turbofan', value: 'TURBOFAN'},
];

const _getInitialState = plane => {
  return {
    inputs: {
      tailNumber: {
        type: 'generic',
        value: plane ? plane.tailNumber : '',
      },
      model: {
        type: 'generic',
        value: plane ? plane.model : '',
      },
      year: {
        type: 'year',
        value: plane ? plane.fabricationYear.toString() : '',
      },
      price: {
        type: 'integer',
        value: plane ? plane.price.toString() : '',
      },
      brand: {
        type: 'generic',
        value: plane ? plane.brand : '',
      },
      engine: {
        type: 'generic',
        value: plane ? plane.engine : '',
      },
    },
  };
};

export class PlaneEdit extends React.Component {
  constructor(props) {
    super(props);
    log(`Props passed to PlaneEdit: ${JSON.stringify(props, null, 2)}`);
    if (props.navigation.state.params) {
      log('Plane exists, retrieving data');
      this.plane = props.navigation.state.params.plane;
    } else {
      this.plane = false;
    }
    log(`Plane value passed to PlaneEdit: ${JSON.stringify(this.plane, null, 2)}`);
    this.state = _getInitialState(this.plane);

    this.onInputChange = validationService.onInputChange.bind(this);
    this.getFormValidation = validationService.getFormValidation.bind(this);
    this.submit = this.submit.bind(this);
  };

  submit(onSubmit) {
    log('Validating form');
    if (this.getFormValidation()) {
      log('Form validated successfully!');
      const newPlane = {
        tailNumber: this.state.inputs.tailNumber.value,
        brand: this.state.inputs.brand.value,
        model: this.state.inputs.model.value,
        price: this.state.inputs.price.value,
        fabricationYear: this.state.inputs.year.value,
        engine: this.state.inputs.engine.value,
      };
      onSubmit(newPlane)
        .then(() => navigation.back())
        .catch(error => {
          log(`Add plane error ${error}`);
        });
    }
  }

  renderError(id) {
    const {inputs} = this.state;
    if (inputs[id].errorLabel) {
      return <Text style={_styles.error}>{inputs[id].errorLabel}</Text>;
    }
    return null;
  }

  render() {
    return (
      <PlaneContext.Consumer>
        {({onSubmit}) => (
          <ScrollView>
            <View style={_styles.container}>
              <View>
                <Text>Tail Number</Text>
                <TextInput
                  editable={this.state.inputs.tailNumber.value.length === 0}
                  style={_styles.input}
                  value={this.state.inputs.tailNumber.value}
                  onChangeText={value => {
                    this.onInputChange({id: 'tailNumber', value});
                  }}
                />
                {this.renderError('tailNumber')}
              </View>
              <View>
                <RNPickerSelect
                  placeholder={{label: 'Select the plane\'s brand'}}
                  value={this.state.inputs.brand.value}
                  onValueChange={(value) =>
                    this.onInputChange({id: 'brand', value})}
                  items={_brandsLabels}
                />
                {this.renderError('brand')}
              </View>
              <View>
                <Text>Model</Text>
                <TextInput
                  style={_styles.input}
                  value={this.state.inputs.model.value}
                  onChangeText={value => {
                    this.onInputChange({id: 'model', value});
                  }}
                />
                {this.renderError('model')}
              </View>
              <View>
                <Text>Fabrication Year</Text>
                <TextInput
                  style={_styles.input}
                  value={this.state.inputs.year.value}
                  onChangeText={value => {
                    this.onInputChange({id: 'year', value});
                  }}
                />
                {this.renderError('year')}
              </View>
              <View>
                <RNPickerSelect
                  placeholder={{label: 'Select the plane\'s engine type'}}
                  value={this.state.inputs.engine.value}
                  onValueChange={(value) => {
                    this.onInputChange({id: 'engine', value});
                  }}
                  items={_engineLabels}
                />
                {this.renderError('engine')}
              </View>
              <View styles={_styles.inputWrapper}>
                <Text>Price</Text>
                <TextInput
                  style={_styles.input}
                  value={this.state.inputs.price.value}
                  onChangeText={value => {
                    this.onInputChange({id: 'price', value});
                  }}
                />
                {this.renderError('price')}
              </View>

              <TouchableOpacity
                style={[_styles.button]}
                onPress={() => {
                  this.submit(onSubmit);
                }}
              >
                <Text>Add plane</Text>
              </TouchableOpacity>
            </View>
          </ScrollView>
        )}
      </PlaneContext.Consumer>
    );
  };
}

PlaneEdit.navigationOptions = () => ({
  headerTitle: 'Plane Edit',
});

const _styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 8,
    paddingTop: 50,
  },
  input: {
    borderWidth: 1,
    borderColor: 'black',
    padding: 10,
    marginBottom: 15,
    alignSelf: 'stretch',
  },
  split: {
    flexDirection: 'row',
  },
  error: {
    position: 'absolute',
    bottom: 0,
    color: 'red',
    fontSize: 12,
  },
  button: {
    alignItems: 'center',
    backgroundColor: '#DDDDDD',
    padding: 10,
    marginVertical: 10,
  },
});
