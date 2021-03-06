import React from 'react';
import {View, TextInput, Alert, StyleSheet, TouchableOpacity, Text, ScrollView, ActivityIndicator} from 'react-native';
import RNPickerSelect from 'react-native-picker-select';
import {getLogger, navigation} from '../../core';
import {PlaneContext} from '../plane-context';
import {validationService} from './service';
import openMap from 'react-native-open-maps';

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
    loading: false,
  };
};

export class PlaneEdit extends React.Component {
  constructor(props) {
    super(props);
    log(`Props passed to PlaneEdit: ${JSON.stringify(props, null, 2)}`);
    if (props.navigation.state.params) {
      log('Plane exists, retrieving data');
      this.update = true;
      this.plane = props.navigation.state.params.plane;
    } else {
      this.update = false;
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
      this.setState({...this.state, loading: true});
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
          this.setState({...this.state, loading: false});
        });
    }
  }

  deletePlane(onDelete) {
    Alert.alert(
      `Delete plane ${this.state.inputs.tailNumber.value}`,
      'Are you sure you want to proceed?',
      [
        {
          text: 'Cancel',
          onPress: () => console.log('Cancel Pressed'),
          style: 'cancel',
        },
        {
          text: 'DELETE', onPress: () => {
            this.setState({...this.state, loading: true});
            onDelete(this.state.inputs.tailNumber.value)
              .then(() => navigation.back())
              .catch(error => {
                log(`Add plane error ${error}`);
                this.setState({...this.state, loading: false});
              });
          },
        },
      ],
      {cancelable: true},
    );
  }

  openGeolocation(onGeolocation) {
    onGeolocation(this.state.inputs.tailNumber.value)
      .then(geolocation => openMap({latitude: geolocation.x, longitude: geolocation.y}))
      .catch(error => log(`Add plane error ${error}`));
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
        {({onSubmit, onUpdate, onDelete, onGeolocation}) => (
          <ScrollView>
            <View style={_styles.container}>
              <View>
                <Text>Tail Number</Text>
                <TextInput
                  editable={this.update === false}
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
                  keyboardType={'numeric'}
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
                  keyboardType={'numeric'}
                />
                {this.renderError('price')}
              </View>

              {(this.update === false) &&
              <TouchableOpacity
                style={[_styles.button]}
                onPress={() => {
                  if (!this.state.loading) {
                    this.submit(onSubmit);
                  }
                }
                }>
                <Text
                  style={this.state.loading ? _styles.disabledText : {}}>ADD PLANE</Text>
              </TouchableOpacity>
              }

              {this.update === true &&
              <TouchableOpacity
                style={this.state.loading ?
                  [_styles.button, _styles.disabledButton] :
                  [_styles.button]
                }
                onPress={() => {
                  this.openGeolocation(onGeolocation);
                }}>
                <Text style={this.state.loading ? _styles.disabledText : {}}>GEOLOCATION</Text>
              </TouchableOpacity>
              }

              {this.update === true &&
              <TouchableOpacity
                style={[_styles.button]}
                onPress={() => {
                  if (!this.state.loading) {
                    this.submit(onUpdate);
                  }
                }}>
                <Text
                  style={this.state.loading ? _styles.disabledText : {}}>UPDATE PLANE</Text>
              </TouchableOpacity>
              }

              {this.update === true &&
              <TouchableOpacity
                style={this.state.loading ?
                  [_styles.button, _styles.deleteButton, _styles.disabledButton] :
                  [_styles.button, _styles.deleteButton]
                }
                onPress={() => {
                  if (!this.state.loading) {
                    this.deletePlane(onDelete);
                  }
                }}>
                <Text style={this.state.loading ? _styles.disabledText : {}}>DELETE PLANE</Text>
              </TouchableOpacity>
              }

              <ActivityIndicator
                animating={this.state.loading}
                size='large'/>
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
  deleteButton: {
    backgroundColor: '#dd0214',
  },
  disabledButton: {
    backgroundColor: '#c6c7ca',
  },
  disabledText: {
    color: '#9b9c9e',
  },
});
