import React from 'react';
import { View, Text, ScrollView, Image, Button, TouchableOpacity , StyleSheet } from 'react-native';
import call from 'react-native-phone-call';
import Icon from 'react-native-vector-icons/FontAwesome';

const TrailInfo = ({ route, navigation }) => {
  const { item } = route.params;

  const handleStartTrail = () => {
    navigation.navigate('MapsScreen', { item });
  };

  const handleBack = () => {
    navigation.navigate('TrailsActivity');
  };

  const handleCallEmergency = () => {
    const phoneNumber = '112';
    const args = {
      number: phoneNumber,
      prompt: false,
    };
    call(args).catch(error => {
      ToastAndroid.show('Failed to make the call!', ToastAndroid.SHORT);
      console.error(error);
    });
  };

  return (
    <View style={styles.container}>
      <ScrollView contentContainerStyle={{ alignItems: 'center', paddingTop: 20 }}>
        <Text style={{ fontSize: 40, marginVertical: 56 }}>{item.trail_name}</Text>
        <Text style={{ fontSize: 20 }}>{item.trail_desc}</Text>
        <Text style={{ fontSize: 20 }}>Duration: {item.trail_duration} minutes</Text>
        <Text style={{ fontSize: 20 }}>Difficulty: {item.trail_difficulty}</Text>
        <Text style={{ fontSize: 20, marginTop: 36 }}>Points of Interest:</Text>
        <Text style={{ fontSize: 20 }}>
          {item.edges.map((edge, index) => (
            <Text key={index}>{edge.edge_start.pin_name}{'\n'}</Text>
          ))}
          {item.edges.length > 0 && item.edges[item.edges.length - 1].edge_end.pin_name}
        </Text>
        <Image source={{ uri: item.trail_img }} style={{ width: 114, height: 120, margin: 10 }} />
        <Button title="Start Trail" onPress={handleStartTrail} />
        <Button title="Back" onPress={handleBack} />
      </ScrollView>

      <View style={styles.emergencyButtonContainer}>
        <TouchableOpacity onPress={handleCallEmergency} style={styles.emergencyButton}>
          <Icon name="phone" size={24} color="red" style={styles.emergencyIcon} />
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  emergencyButtonContainer: {
    position: 'absolute',
    bottom: 0,
    right: 16,
  },
  emergencyIcon: {
    marginRight: 10,
  },
});

export default TrailInfo;
