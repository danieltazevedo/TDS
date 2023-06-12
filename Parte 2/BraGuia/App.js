import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import PaginaInicial from './Screens/pagina-inicial';
import Login from './Screens/login';
import TrailsActivity from './Screens/trailsActivity'
import TrailInfo from './Screens/trailInfo'
import MapsScreen from './Screens/mapsScreen'
import MarkerInfo from './Screens/markerInfo'
import Socials from './Screens/socials';
import Partners from './Screens/partners';
import Contacts from './Screens/contacts';

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
        <Stack.Screen name="Socials" component={Socials} />
        <Stack.Screen name="Partners" component={Partners} />
        <Stack.Screen name="Contacts" component={Contacts} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
