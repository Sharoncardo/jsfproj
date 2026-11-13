import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { authApi } from '../api/authApi';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const logout = useCallback(() => {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    setUser(null);
  }, []);

  const loadUser = useCallback(async () => {
    const token = localStorage.getItem('access_token');
    if (!token) {
      setLoading(false);
      return;
    }

    try {
      const res = await authApi.getCurrentUser();
      if (res.success && res.data) {
        setUser(res.data);
      } else {
        logout();
      }
    } catch (err) {
      console.warn('Failed to fetch current user profile:', err);
      logout();
    } finally {
      setLoading(false);
    }
  }, [logout]);

  useEffect(() => {
    loadUser();

    const handleAuthLogout = () => {
      logout();
    };

    window.addEventListener('auth:logout', handleAuthLogout);
    return () => window.removeEventListener('auth:logout', handleAuthLogout);
  }, [loadUser, logout]);

  const login = async (usernameOrEmail, password) => {
    const res = await authApi.login({ usernameOrEmail, password });
    if (res.success && res.data) {
      localStorage.setItem('access_token', res.data.accessToken);
      localStorage.setItem('refresh_token', res.data.refreshToken);
      setUser(res.data.user);
      return res.data;
    }
    throw new Error(res.message || 'Login failed');
  };

  const register = async (userData) => {
    const res = await authApi.register(userData);
    if (res.success && res.data) {
      localStorage.setItem('access_token', res.data.accessToken);
      localStorage.setItem('refresh_token', res.data.refreshToken);
      setUser(res.data.user);
      return res.data;
    }
    throw new Error(res.message || 'Registration failed');
  };

  const updateUser = async (profileData) => {
    const res = await authApi.updateProfile(profileData);
    if (res.success && res.data) {
      setUser(res.data);
      return res.data;
    }
    throw new Error(res.message || 'Profile update failed');
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        isAuthenticated: !!user,
        login,
        register,
        logout,
        updateUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
