import { useContext } from 'react';
import { UIContext } from '../contexts/UIContext';
import type { UIContextState } from '../types';

/**
 * Custom hook providing access to UIContext state and methods.
 */
export const useUI = (): UIContextState => {
  const context = useContext(UIContext);
  if (context === undefined) {
    throw new Error('useUI must be used within a UIProvider');
  }
  return context;
};

export default useUI;
