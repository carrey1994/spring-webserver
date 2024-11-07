// src/components/Dashboard.tsx
import React from 'react';
import { Box, Typography } from '@mui/material';

const Dashboard: React.FC = () => {
  return (
    <Box 
      display="flex" 
      justifyContent="center" 
      alignItems="center" 
      minHeight="100vh"
      bgcolor="#e0f7fa"
    >
      <Typography variant="h3">Welcome to the Dashboard</Typography>
    </Box>
  );
};

export default Dashboard;
