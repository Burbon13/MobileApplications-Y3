// import React, {Component} from 'react';
// import {Text, View, StyleSheet} from 'react-native';
//
// const styles = StyleSheet.create({
//   bigBlue: {
//     color: 'blue',
//     fontWeight: 'bold',
//     fontSize: 30,
//   },
//   red: {
//     color: 'red',
//   },
// });
//
// class Blink extends Component {
//
//   componentDidMount() {
//     // Toggle the state every second
//     setInterval(() => (
//       this.setState(previousState => (
//         {isShowingText: !previousState.isShowingText}
//       ))
//     ), Math.random() * 10000);
//   }
//
//   //state object
//   state = {isShowingText: true};
//
//   render() {
//     if (!this.state.isShowingText) {
//       return null;
//     }
//
//     return (
//       <View>
//         <Text style={styles.red}>{this.props.text}</Text>
//         <Text style={styles.bigBlue}>{this.props.text}</Text>
//         <Text style={[styles.bigBlue, styles.red]}>{this.props.text}</Text>
//       </View>
//     );
//   }
// }
//
// // export default class BlinkApp extends Component {
// //   render() {
// //     return (
// //       <View>
// //         <View style={{width: 50, height: 50, backgroundColor: 'magenta'}}/>
// //         <Blink text='I love to blink'/>
// //         <Blink text='Yes blinking is so great'/>
// //         <Blink text='Why did they ever take this out of HTML'/>
// //         <View style={{width: 100, height: 100, backgroundColor: 'green'}}/>
// //         <Blink text='Look at me look at me look at me'/>
// //         <View style={{width: 150, height: 150, backgroundColor: 'steelblue'}}/>
// //       </View>
// //     );
// //   }
// // }
//
//
// export default class FlexDimensionsBasics extends Component {
//   render() {
//     return (
//       // Try removing the `flex: 1` on the parent View.
//       // The parent will not have dimensions, so the children can't expand.
//       // What if you add `height: 300` instead of `flex: 1`?
//       <View style={{flex: 1}}>
//         <View style={{flex: 1, flexDirection: 'row'}}>
//           <View style={{flex: 1, backgroundColor: 'powderblue'}}/>
//           <View style={{flex: 2, backgroundColor: 'skyblue'}}/>
//           <View style={{flex: 3, backgroundColor: 'steelblue'}}/>
//         </View>
//         <View style={{flex: 2, flexDirection: 'column-reverse'}}>
//           <View style={{flex: 1, backgroundColor: 'red'}}/>
//           <View style={{flex: 2, backgroundColor: 'magenta'}}/>
//           <View style={{flex: 3, backgroundColor: 'black'}}/>
//         </View>
//       </View>
//     );
//   }
// }


import React from 'react';
import {FlatList, ActivityIndicator, Text, View} from 'react-native';

export default class FetchExample extends React.Component {

  constructor(props) {
    super(props);
    this.state = {isLoading: true};
  }

  componentDidMount() {
    return fetch('https://facebook.github.io/react-native/movies.json')
      .then((response) => response.json())
      .then((responseJson) => {

        this.setState({
          isLoading: false,
          dataSource: responseJson.movies,
        }, function () {

        });

      })
      .catch((error) => {
        console.error(error);
      });
  }


  render() {

    if (this.state.isLoading) {
      return (
        <View style={{flex: 1, padding: 20}}>
          <ActivityIndicator/>
        </View>
      );
    }

    return (
      <View style={{flex: 1, paddingTop: 20}}>
        <FlatList
          data={this.state.dataSource}
          renderItem={({item}) => <Text>{item.title}, {item.releaseYear}</Text>}
          keyExtractor={({id}, index) => id}
        />
      </View>
    );
  }
}
