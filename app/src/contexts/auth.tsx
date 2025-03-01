import React, { createContext, useContext, useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { User, UserProfile } from '@/types/user';
import { axiosInstance } from '@/lib/axios';
import { useToast } from '@/hooks/use-toast';

interface AuthContextType extends UserProfile {
  login: (token: string) => void;
  logout: () => void;
  updateProfile: (user: User) => void;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const navigate = useNavigate();
  const { toast } = useToast();

  const fetchUserProfile = useCallback(async () => {
    try {
      const response = await axiosInstance.get<User>('/api/v1/members/me');
      setUser(response.data);
      setIsAuthenticated(true);
    } catch (error) {
      console.error('Failed to fetch user profile:', error);
      toast({
        variant: "destructive",
        title: "오류 발생",
        description: "사용자 정보를 불러오는데 실패했습니다.",
      });
      logout();
    } finally {
      setIsLoading(false);
    }
  }, [toast]);

  const login = useCallback((token: string) => {
    localStorage.setItem('access_token', token);
    setIsAuthenticated(true);
    fetchUserProfile();
  }, [fetchUserProfile]);

  const logout = useCallback(() => {
    localStorage.removeItem('access_token');
    setUser(null);
    setIsAuthenticated(false);
    setIsLoading(false);
    navigate('/login');
  }, [navigate]);

  const updateProfile = useCallback((updatedUser: User) => {
    setUser(updatedUser);
  }, []);

  useEffect(() => {
    const token = localStorage.getItem('access_token');
    if (token) {
      fetchUserProfile();
    } else {
      setIsLoading(false);
    }
  }, [fetchUserProfile]);

  const value = {
    user,
    isAuthenticated,
    isLoading,
    login,
    logout,
    updateProfile,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
