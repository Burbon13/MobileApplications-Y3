import React, {useContext} from 'react';
import {ActivityIndicator, Button, FlatList, Text, View} from 'react-native';

import {getLogger, navigation} from '../core';
import {PlaneContext} from './plane-context';
import {AuthContext} from '../auth/context';
import Plane from './plane';

const log = getLogger('PlaneList');

export const PlaneList = ({navigation}) => {
  log('Rendering');
  const {onLogout} = useContext(AuthContext);
  return (
    <View>
      <Button
        onPress={() => onLogout().then(() => navigation.navigate('Auth'))}
        title="Sign Out"
      />
      <PlaneContext.Consumer>
        {({isLoading, loadingError, planes}) => {
          return (
            <View>
              <ActivityIndicator animating={!!isLoading} size="large"/>
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
    </View>
  )
};

PlaneList.navigationOptions = {
  headerTitle: 'Plane List',
  headerRight: (
    <Button
      onPress={() => navigation.navigate('PlaneEdit')}
      title="Add Plane <3"
    />
  ),
};
