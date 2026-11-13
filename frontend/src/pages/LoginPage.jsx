import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Code2, Lock, User, AlertCircle, ArrowRight, Sparkles } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const loginSchema = z.object({
  usernameOrEmail: z.string().min(1, 'Please enter your username or email'),
  password: z.string().min(1, 'Please enter your password'),
});

export const LoginPage = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [serverError, setServerError] = useState('');

  const from = location.state?.from?.pathname || '/dashboard';

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors, isSubmitting },
  } = useForm({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = async (data) => {
    setServerError('');
    try {
      await login(data.usernameOrEmail, data.password);
      navigate(from, { replace: true });
    } catch (err) {
      setServerError(
        err.response?.data?.message || err.message || 'Invalid username or password'
      );
    }
  };

  const handleFillDemo = (username, password) => {
    setValue('usernameOrEmail', username, { shouldValidate: true });
    setValue('password', password, { shouldValidate: true });
  };

  return (
    <div className="min-h-[85vh] flex items-center justify-center px-4 py-12">
      <div className="w-full max-w-md">
        {/* Brand Header */}
        <div className="text-center mb-8">
          <div className="inline-flex p-3 rounded-2xl bg-gradient-to-tr from-indigo-600 to-purple-600 text-white shadow-xl shadow-indigo-500/25 mb-4">
            <Code2 className="w-8 h-8" />
          </div>
          <h1 className="text-2xl font-extrabold text-white tracking-tight">Welcome back</h1>
          <p className="text-xs text-slate-400 mt-1">
            Sign in to track your DSA mastery, streaks, and personal mnemonics
          </p>
        </div>

        {/* Card */}
        <div className="p-8 rounded-2xl bg-slate-900 border border-slate-800 shadow-2xl">
          {/* Quick Demo Credentials */}
          <div className="p-3 mb-6 rounded-xl bg-indigo-500/10 border border-indigo-500/20 text-xs">
            <div className="flex items-center space-x-1.5 text-indigo-300 font-semibold mb-2">
              <Sparkles className="w-3.5 h-3.5 text-indigo-400" />
              <span>Quick Demo Sign-In:</span>
            </div>
            <div className="flex gap-2">
              <button
                type="button"
                onClick={() => handleFillDemo('demo_user', 'Password123!')}
                className="flex-1 py-1.5 px-2 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 text-[11px] font-medium transition-colors"
              >
                Demo User
              </button>
              <button
                type="button"
                onClick={() => handleFillDemo('admin', 'Password123!')}
                className="flex-1 py-1.5 px-2 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 text-[11px] font-medium transition-colors"
              >
                Admin User
              </button>
            </div>
          </div>

          {serverError && (
            <div className="flex items-start space-x-2 p-3.5 mb-5 rounded-xl bg-rose-500/10 border border-rose-500/20 text-rose-300 text-xs">
              <AlertCircle className="w-4 h-4 mt-0.5 flex-shrink-0" />
              <span>{serverError}</span>
            </div>
          )}

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1.5">
                Username or Email
              </label>
              <div className="relative">
                <User className="absolute left-3.5 top-3 w-4 h-4 text-slate-500" />
                <input
                  type="text"
                  {...register('usernameOrEmail')}
                  placeholder="demo_user or demo@example.com"
                  className={`w-full pl-10 pr-3.5 py-2.5 text-xs bg-slate-800/90 border rounded-xl text-white placeholder-slate-500 focus:outline-none transition-colors ${
                    errors.usernameOrEmail
                      ? 'border-rose-500 focus:border-rose-500'
                      : 'border-slate-700 focus:border-indigo-500'
                  }`}
                />
              </div>
              {errors.usernameOrEmail && (
                <p className="mt-1 text-[11px] text-rose-400">{errors.usernameOrEmail.message}</p>
              )}
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1.5">
                Password
              </label>
              <div className="relative">
                <Lock className="absolute left-3.5 top-3 w-4 h-4 text-slate-500" />
                <input
                  type="password"
                  {...register('password')}
                  placeholder="••••••••••••"
                  className={`w-full pl-10 pr-3.5 py-2.5 text-xs bg-slate-800/90 border rounded-xl text-white placeholder-slate-500 focus:outline-none transition-colors ${
                    errors.password
                      ? 'border-rose-500 focus:border-rose-500'
                      : 'border-slate-700 focus:border-indigo-500'
                  }`}
                />
              </div>
              {errors.password && (
                <p className="mt-1 text-[11px] text-rose-400">{errors.password.message}</p>
              )}
            </div>

            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full mt-2 flex items-center justify-center space-x-2 py-3 px-4 rounded-xl text-xs font-bold text-white bg-indigo-600 hover:bg-indigo-500 shadow-lg shadow-indigo-600/30 transition-all disabled:opacity-50"
            >
              <span>{isSubmitting ? 'Signing in...' : 'Sign In'}</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </form>

          <div className="mt-6 text-center text-xs text-slate-400">
            Don't have an account?{' '}
            <Link to="/register" className="text-indigo-400 hover:text-indigo-300 font-semibold">
              Create an account
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};
