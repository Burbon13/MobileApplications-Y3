import React, {useContext} from 'react';
import {ActivityIndicator, Button, FlatList, StyleSheet, Text, View} from 'react-native';
import {FloatingAction} from "react-native-floating-action";
import {getLogger, navigation} from '../core';
import {PlaneContext} from './plane-context';
import {AuthContext} from '../auth/context';
import Plane from './plane';

const log = getLogger('PlaneList');

const _actions = [
  {
    text: "Add plane",
    icon: require("../../assets/images/airplane.png"),
    name: "bt_add_plane",
    position: 1
  },
  {
    text: "Stats",
    icon: require("../../assets/images/pie-chart.png"),
    name: "bt_stats",
    position: 2
  },
  {
    text: "Logout",
    icon: require("../../assets/images/logout.png"),
    name: "bt_logout",
    position: 3
  },
];

export const PlaneList = ({navigation}) => {
  log('Rendering');
  const {onLogout} = useContext(AuthContext);
  return (
    <View style={_styles.container}>
      <PlaneContext.Consumer>
        {({isLoading, loadingError, planes}) => {
          return (
            <View>
              {isLoading && <ActivityIndicator size="large"/>}
              {loadingError && <Text>{loadingError.message || 'Loading error'}</Text>}
              {planes &&
              <FlatList
                data={planes}
                renderItem={(plane) => <Plane plane={plane}/>}
                keyExtractor={plane => plane.tailNumber}
              />}
            </View>
          )
        }}
      </PlaneContext.Consumer>
      <FloatingAction
        actions={_actions}
        onPressItem={name => {
          switch (name) {
            case 'bt_add_plane':
              navigation.navigate('PlaneEdit');
              break;
            case 'bt_stats':
              navigation.navigate('BrandsPieChart');
              break;
            case 'bt_logout':
              onLogout().then(() => navigation.navigate('Auth'))
              break;
          }
        }}/>
    </View>
  )
};

PlaneList.navigationOptions = {
  headerTitle: 'Plane List',
};


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
