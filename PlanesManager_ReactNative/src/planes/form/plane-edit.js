import React from 'react';
import {View, TextInput, Picker, StyleSheet, TouchableOpacity, Text, ScrollView} from 'react-native';
import RNPickerSelect from 'react-native-picker-select'
import {getLogger, navigation} from '../../core';
import {PlaneContext} from "../plane-context";
import {validationService} from "./service";

const log = getLogger('PlaneEdit');

export class PlaneEdit extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      inputs: {
        tailNumber: {
          type: "generic",
          value: ""
        },
        model: {
          type: "generic",
          value: ""
        },
        year: {
          type: "year",
          value: ""
        },
        price: {
          type: "integer",
          value: ""
        },
        brand: {
          type: "generic",
          value: ""
        },
        engine: {
          type: "generic",
          value: ""
        }
      }
    };

    this.onInputChange = validationService.onInputChange.bind(this);
    this.getFormValidation = validationService.getFormValidation.bind(this);
    this.submit = this.submit.bind(this);
  };

  submit() {
    console.log(this.getFormValidation());
  }

  renderError(id) {
    const {inputs} = this.state;
    if (inputs[id].errorLabel) {
      return <Text style={_styles.error}>{inputs[id].errorLabel}</Text>;
    }
    return null;
  }

  render() {
    const {tailNumber, brand, model, year, price, engine} = this.state;
    return (
      <PlaneContext.Consumer>
        {({onSubmit}) => (
          <ScrollView>
            <View style={_styles.container}>
              <View>
                <Text>Tail Number</Text>
                <TextInput
                  style={_styles.input}
                  onChangeText={value => {
                    this.onInputChange({id: "tailNumber", value});
                  }}
                />
                {this.renderError("tailNumber")}
              </View>
              <View>
                <RNPickerSelect
                  placeholder={{label: 'Select the plane\'s brand'}}
                  onValueChange={(value) =>
                    this.onInputChange({id: "brand", value})}
                  items={[
                    {label: 'Boeing', value: 'BOEING'},
                    {label: 'Airbus', value: 'AIRBUS'},
                    {label: 'ATR', value: 'ATR'},
                    {label: 'Embraer', value: 'EMBRAER'},
                    {label: 'Bombardier', value: 'BOMBARDIER'},
                  ]}
                />
                {this.renderError("brand")}
              </View>
              <View>
                <Text>Model</Text>
                <TextInput
                  style={_styles.input}
                  onChangeText={value => {
                    this.onInputChange({id: "model", value});
                  }}
                />
                {this.renderError("model")}
              </View>
              <View>
                <Text>Fabrication Year</Text>
                <TextInput
                  style={_styles.input}
                  onChangeText={value => {
                    this.onInputChange({id: "year", value});
                  }}
                />
                {this.renderError("year")}
              </View>
              <View>
                <RNPickerSelect
                  placeholder={{label: 'Select the plane\'s engine type'}}
                  onValueChange={(value) => {
                    this.onInputChange({id: "engine", value});
                  }}
                  items={[
                    {label: 'Turboprop', value: 'TURBOPROP'},
                    {label: 'Ramjet', value: 'RAMJET'},
                    {label: 'Turbojet', value: 'TURBOJET'},
                    {label: 'Turbofan', value: 'TURBOFAN'},
                  ]}
                />
                {this.renderError("engine")}
              </View>
              <View styles={_styles.inputWrapper}>
                <Text>Price</Text>
                <TextInput
                  style={_styles.input}
                  onChangeText={value => {
                    this.onInputChange({id: "price", value});
                  }}
                />
                {this.renderError("price")}
              </View>

              <TouchableOpacity
                style={[_styles.button]}
                onPress={() => {
                  log('Plane submit');
                  this.submit();
                  // onSubmit(value)
                  //   .then(() => navigation.goBack())
                  //   .catch(error => {
                  //     log(`Add plane error ${error}`)
                  //   });
                }}
              >
                <Text>Add plane</Text>
              </TouchableOpacity>
            </View>
          </ScrollView>
        )}
      </PlaneContext.Consumer>
    )
  };
}

PlaneEdit.navigationOptions = () => ({
  headerTitle: 'Plane Edit',
});

const _styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 8,
    paddingTop: 50
  },
  input: {
    borderWidth: 1,
    borderColor: "black",
    padding: 10,
    marginBottom: 15,
    alignSelf: "stretch"
  },
  split: {
    flexDirection: "row"
  },
  error: {
    position: "absolute",
    bottom: 0,
    color: "red",
    fontSize: 12
  },
  button: {
    alignItems: 'center',
    backgroundColor: '#DDDDDD',
    padding: 10,
    marginVertical: 10,
  }
});
