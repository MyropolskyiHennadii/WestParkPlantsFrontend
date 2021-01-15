import React from 'react';
import './App.css';
import RemoteDataComponent from './components/RemoteDataComponent';
import { useTranslation } from 'react-i18next';

function App() {
  const [t, i18n] = useTranslation();
  return (
      <div className="App">
        <RemoteDataComponent t={t} />
      </div>
  );
}

export default App;