import ThemeProvider from './providers/theme'
import AppRoutes from './routes'

function App() {
  return (
    <ThemeProvider>
      <AppRoutes />
    </ThemeProvider>
  )
}

export default App

