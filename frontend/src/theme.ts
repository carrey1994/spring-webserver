// src/theme.ts
import { createTheme } from '@mui/material/styles';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',  // Set the palette mode to dark
    primary: {
      main: '#90caf9',  // Custom primary color
    },
    secondary: {
      main: '#f48fb1',  // Custom secondary color
    },
  },
});

export default darkTheme;
