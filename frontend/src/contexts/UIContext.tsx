/* eslint-disable react-refresh/only-export-components */
import React, { createContext, useState, useCallback } from 'react';
import type { UIContextState } from '../types';

export const UIContext = createContext<UIContextState | undefined>(undefined);

export const UIProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [sidebarOpen, setSidebarOpen] = useState<boolean>(true);
  const [mobileSidebarOpen, setMobileSidebarOpen] = useState<boolean>(false);

  const toggleSidebar = useCallback(() => {
    setSidebarOpen((prev) => !prev);
  }, []);

  const openSidebar = useCallback(() => {
    setSidebarOpen(true);
  }, []);

  const closeSidebar = useCallback(() => {
    setSidebarOpen(false);
  }, []);

  const toggleMobileSidebar = useCallback(() => {
    setMobileSidebarOpen((prev) => !prev);
  }, []);

  const openMobileSidebar = useCallback(() => {
    setMobileSidebarOpen(true);
  }, []);

  const closeMobileSidebar = useCallback(() => {
    setMobileSidebarOpen(false);
  }, []);

  return (
    <UIContext.Provider
      value={{
        sidebarOpen,
        mobileSidebarOpen,
        toggleSidebar,
        openSidebar,
        closeSidebar,
        toggleMobileSidebar,
        openMobileSidebar,
        closeMobileSidebar,
      }}
    >
      {children}
    </UIContext.Provider>
  );
};
