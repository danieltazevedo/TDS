import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet, ToastAndroid } from 'react-native';
import axios from 'axios';


const Login = ({ navigation }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");


  const handleBack = () => {
    navigation.navigate('PaginaInicial');
  };

  const handleLogin = () => {
    if (username === '' || password === '') {
      ToastAndroid.show('Please fill in all fields!', ToastAndroid.SHORT);
      return;
    }
    
    let json_body = JSON.stringify({username,password});

    axios.post("https://c5a2-193-137-92-29.eu.ngrok.io/login", json_body, {
      withCredentials: false,
      headers: {
        "Content-Type" : "application/json",
      },
    })
    .then((response) => {
        if (response.status === 200) {
          

          navigation.navigate('TrailsActivity');
          
        } else {
          ToastAndroid.show('Incorrect username or password!', ToastAndroid.SHORT);
        }
    })
      .catch(error => {
        ToastAndroid.show('Incorrect username or password!', ToastAndroid.SHORT);
        console.error(error);
      });      
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Sign in</Text>
      <TextInput
        style={styles.input}
        placeholder="Username"
        keyboardType="default"
        value={username}
        onChangeText={text => setUsername(text)}
      />
      <TextInput
        style={styles.input}
        placeholder="Password"
        secureTextEntry={true}
        value={password}
        onChangeText={text => setPassword(text)}
      />
      <Button title="Login" onPress={handleLogin} />
      <Button title="Back" onPress={handleBack} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    padding: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 16,
  },
  input: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    marginBottom: 16,
    paddingHorizontal: 8,
  },
});

export default Login;
