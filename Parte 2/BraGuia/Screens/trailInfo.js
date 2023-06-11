import React from 'react';
import { View, Text, ScrollView, Image, Button } from 'react-native';

const TrailInfo = ({ route, navigation }) => {
  const { item } = route.params;

  const handleStartTrail = () => {
    navigation.navigate('Maps_Activity', { item });
  };

  const handleBack = () => {
    navigation.navigate('TrailsActivity');
  };

  return (
    <ScrollView>
      <View style={{ alignItems: 'center' }}>
        <Text style={{ fontSize: 40, marginVertical: 56 }}>{item.trail_name}</Text>
        <Text style={{ fontSize: 20 }}>{item.trail_desc}</Text>
        <Text style={{ fontSize: 20 }}>Duration: {item.trail_duration} minutes</Text>
        <Text style={{ fontSize: 20 }}>Difficulty: {item.trail_difficulty}</Text>
        <Text style={{ fontSize: 20, marginTop: 36 }}>Points of Interest:</Text>
        <Text style={{ fontSize: 20 }}>
          {item.edges.map((edge, index) => (
            <Text key={index}>{edge.edge_start.pin_name}{'\n'}</Text>
          ))}
          {item.edges.length > 0 &&  item.edges[item.edges.length - 1].edge_end.pin_name}
        </Text>
        <Image source={{ uri: item.trail_img }} style={{ width: 114, height: 120, margin: 10 }} />
        <Button title="Start Trail" onPress={handleStartTrail} />
        <Button title="Back" onPress={handleBack} />
      </View>
    </ScrollView>
  );
};

export default TrailInfo;
