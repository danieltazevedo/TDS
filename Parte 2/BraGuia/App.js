import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import PaginaInicial from './Screens/pagina-inicial';
import Login from './Screens/login';
import TrailsActivity from './Screens/trailsActivity'
import TrailInfo from './Screens/trailInfo'
import MapsScreen from './Screens/mapsScreen'
import MarkerInfo from './Screens/markerInfo'

const Stack = createNativeStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="PaginaInicial" component={PaginaInicial} />
        <Stack.Screen name="Login" component={Login} />
        <Stack.Screen name="TrailsActivity" component={TrailsActivity} />
        <Stack.Screen name="TrailInfo" component={TrailInfo} />
        <Stack.Screen name="MapsScreen" component={MapsScreen} />
        <Stack.Screen name="MarkerInfo" component={MarkerInfo} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
