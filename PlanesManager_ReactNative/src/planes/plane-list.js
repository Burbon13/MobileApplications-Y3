import React, {useContext} from 'react';
import {ActivityIndicator, Button, FlatList, Text, View} from 'react-native';

import {getLogger} from '../core';
import {PlaneContext} from './plane-context';
import {AuthContext} from '../auth/context';
import Plane from './plane';

const log = getLogger('PlaneList');

export const PlaneList = ({navigation}) => {
  log('render');
  const {onSignOut} = useContext(AuthContext);
  return (
    <View>
      <PlaneContext.Consumer>
        {({isLoading, loadingError, items: planes}) => (
          <View>
            <ActivityIndicator animating={!!isLoading} size="large"/>
            {loadingError && <Text>{loadingError.message || 'Loading error'}</Text>}
            {planes &&
            <FlatList
              data={planes.map(plane => ({...plane, key: plane.id}))}
              renderItem={({item: plane}) => <Plane item={plane}/>}
            />}
          </View>
        )}
      </PlaneContext.Consumer>
      <Button
        onPress={() => onSignOut().then(() => navigation.navigate('Auth'))}
        title="Sign Out"
      />
    </View>
  )
};

PlaneList.navigationOptions = {
  headerTitle: 'Plane List',
  headerRight: (
    <Button
      onPress={() => navigator.navigate('PlaneEdit')}
      title="Add Plane <3"
    />
  ),
};
