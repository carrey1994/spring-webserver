// src/components/LoginPage.tsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Button, TextField, Typography, Card, CardContent, Avatar } from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import axios from 'axios';

const LoginPage: React.FC = () => {
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [message, setMessage] = useState<string>('');
  const [token, setToken] = useState<string>('');
  const navigate = useNavigate();  // Use useNavigate for redirection

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/v1/public/login', {
        username,
        password,
      });
      const { "access-token": accessToken } = response.data.result;
      setMessage('Login successful');
      setToken(accessToken);
      navigate('/dashboard');  // Redirect to dashboard
    } catch (error: any) {
      setMessage(`Login failed: ${error.response?.data?.message || error.message}`);
    }
  };

  return (
    <Box 
      display="flex" 
      justifyContent="center" 
      alignItems="center" 
      minHeight="100vh"
      bgcolor="#121212"  // Dark background color
    >
      <Card sx={{ maxWidth: 400, padding: 4, borderRadius: 4, boxShadow: 3, bgcolor: '#1e1e1e' }}>
        <CardContent>
          <Box display="flex" justifyContent="center" alignItems="center" mb={3}>
            <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
              <LockOutlinedIcon />
            </Avatar>
          </Box>
          <Typography variant="h5" component="h1" align="center" gutterBottom color="text.primary">
            Login
          </Typography>
          <form onSubmit={handleLogin}>
            <TextField
              variant="outlined"
              margin="normal"
              fullWidth
              label="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              InputProps={{
                style: { color: '#ffffff' },  // White text color for input
              }}
              InputLabelProps={{
                style: { color: '#ffffff' },  // White label color
              }}
              sx={{ input: { color: 'white' }, label: { color: 'white' } }}
            />
            <TextField
              variant="outlined"
              margin="normal"
              fullWidth
              label="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              InputProps={{
                style: { color: '#ffffff' },  // White text color for input
              }}
              InputLabelProps={{
                style: { color: '#ffffff' },  // White label color
              }}
              sx={{ input: { color: 'white' }, label: { color: 'white' } }}
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
              sx={{ mt: 2, mb: 2 }}
            >
              Login
            </Button>
          </form>
          {message && (
            <Typography color="error" align="center" variant="body2">
              {message}
            </Typography>
          )}
          {token && (
            <Typography color="primary" align="center" variant="body2">
              Access Token: {token}
            </Typography>
          )}
        </CardContent>
      </Card>
    </Box>
  );
};

export default LoginPage;
