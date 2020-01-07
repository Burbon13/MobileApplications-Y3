import React from 'react';
import {Animated, Easing} from 'react-native';

export const PlaneIconAnimation = (props) => {
  const [fadeAnim] = React.useState(new Animated.Value(0));  // Initial value for opacity: 0
  const [spinAnim] = React.useState(new Animated.Value(0));

  React.useEffect(() => {
    Animated.parallel([
      Animated.loop(
        Animated.sequence([
          Animated.timing(
            fadeAnim,
            {
              toValue: 1,
              duration: 2000,
            },
          ),
          Animated.timing(
            fadeAnim,
            {
              toValue: 0,
              duration: 2000,
            },
          ),
        ]),
      ),
      Animated.loop(
        Animated.timing(
          spinAnim,
          {
            toValue: 1,
            duration: 18000,
            easing: Easing.linear,
          },
        ),
      ),
    ]).start();
  }, []);

  return (
    <Animated.View
      style={{
        ...props.style,
        opacity: fadeAnim,
        transform: [{
          rotate: spinAnim.interpolate({
            inputRange: [0, 1],
            outputRange: ['0deg', '360deg'],
          }),
        }],
      }}
    >
      {props.children}
    </Animated.View>
  );
};
