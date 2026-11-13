import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Link, useNavigate } from 'react-router-dom';
import { Code2, Lock, Mail, User, Check, AlertCircle, ArrowRight, ShieldCheck } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const registerSchema = z
  .object({
    username: z
      .string()
      .min(3, 'Username must be at least 3 characters')
      .max(50, 'Username cannot exceed 50 characters')
      .regex(/^[a-zA-Z0-9_-]+$/, 'Only letters, numbers, underscores, and hyphens allowed'),
    email: z.string().email('Please enter a valid email address'),
    displayName: z.string().max(100, 'Display name cannot exceed 100 characters').optional(),
    password: z
      .string()
      .min(8, 'Password must be at least 8 characters long')
      .regex(/[A-Z]/, 'Password must contain at least one uppercase letter')
      .regex(/[a-z]/, 'Password must contain at least one lowercase letter')
      .regex(/[0-9]/, 'Password must contain at least one digit'),
    confirmPassword: z.string().min(1, 'Please confirm your password'),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords do not match",
    path: ['confirmPassword'],
  });

export const RegisterPage = () => {
  const { register: registerAuth } = useAuth();
  const navigate = useNavigate();
  const [serverError, setServerError] = useState('');

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors, isSubmitting },
  } = useForm({
    resolver: zodResolver(registerSchema),
    mode: 'onChange',
  });

  const passwordValue = watch('password', '');

  // Calculate password strength
  const getPasswordStrength = (pwd) => {
    if (!pwd) return { score: 0, label: 'Too short', color: 'bg-slate-700' };
    let score = 0;
    if (pwd.length >= 8) score++;
    if (/[A-Z]/.test(pwd)) score++;
    if (/[a-z]/.test(pwd)) score++;
    if (/[0-9]/.test(pwd)) score++;
    if (/[^A-Za-z0-9]/.test(pwd)) score++;

    if (score <= 2) return { score: 25, label: 'Weak', color: 'bg-rose-500' };
    if (score === 3) return { score: 50, label: 'Fair', color: 'bg-amber-500' };
    if (score === 4) return { score: 75, label: 'Good', color: 'bg-blue-500' };
    return { score: 100, label: 'Strong', color: 'bg-emerald-500' };
  };

  const strength = getPasswordStrength(passwordValue);

  const onSubmit = async (data) => {
    setServerError('');
    try {
      await registerAuth({
        username: data.username,
        email: data.email,
        password: data.password,
        displayName: data.displayName || data.username,
      });
      navigate('/dashboard');
    } catch (err) {
      setServerError(
        err.response?.data?.message || err.message || 'Registration failed. Please try again.'
      );
    }
  };

  return (
    <div className="min-h-[85vh] flex items-center justify-center px-4 py-12">
      <div className="w-full max-w-md">
        {/* Brand Header */}
        <div className="text-center mb-8">
          <div className="inline-flex p-3 rounded-2xl bg-gradient-to-tr from-indigo-600 to-purple-600 text-white shadow-xl shadow-indigo-500/25 mb-4">
            <Code2 className="w-8 h-8" />
          </div>
          <h1 className="text-2xl font-extrabold text-white tracking-tight">Create your account</h1>
          <p className="text-xs text-slate-400 mt-1">
            Master Data Structures & Algorithms with personalized metrics and mnemonics
          </p>
        </div>

        {/* Card */}
        <div className="p-8 rounded-2xl bg-slate-900 border border-slate-800 shadow-2xl">
          {serverError && (
            <div className="flex items-start space-x-2 p-3.5 mb-5 rounded-xl bg-rose-500/10 border border-rose-500/20 text-rose-300 text-xs">
              <AlertCircle className="w-4 h-4 mt-0.5 flex-shrink-0" />
              <span>{serverError}</span>
            </div>
          )}

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            {/* Username */}
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1.5">
                Username
              </label>
              <div className="relative">
                <User className="absolute left-3.5 top-3 w-4 h-4 text-slate-500" />
                <input
                  type="text"
                  {...register('username')}
                  placeholder="alex_coder"
                  className={`w-full pl-10 pr-3.5 py-2.5 text-xs bg-slate-800/90 border rounded-xl text-white placeholder-slate-500 focus:outline-none transition-colors ${
                    errors.username
                      ? 'border-rose-500 focus:border-rose-500'
                      : 'border-slate-700 focus:border-indigo-500'
                  }`}
                />
              </div>
              {errors.username && (
                <p className="mt-1 text-[11px] text-rose-400">{errors.username.message}</p>
              )}
            </div>

            {/* Email */}
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1.5">
                Email Address
              </label>
              <div className="relative">
                <Mail className="absolute left-3.5 top-3 w-4 h-4 text-slate-500" />
                <input
                  type="email"
                  {...register('email')}
                  placeholder="alex@example.com"
                  className={`w-full pl-10 pr-3.5 py-2.5 text-xs bg-slate-800/90 border rounded-xl text-white placeholder-slate-500 focus:outline-none transition-colors ${
                    errors.email
                      ? 'border-rose-500 focus:border-rose-500'
                      : 'border-slate-700 focus:border-indigo-500'
                  }`}
                />
              </div>
              {errors.email && (
                <p className="mt-1 text-[11px] text-rose-400">{errors.email.message}</p>
              )}
            </div>

            {/* Display Name */}
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1.5">
                Full Name / Display Name (optional)
              </label>
              <input
                type="text"
                {...register('displayName')}
                placeholder="Alex Rivera"
                className="w-full px-3.5 py-2.5 text-xs bg-slate-800/90 border border-slate-700 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:border-indigo-500 transition-colors"
              />
            </div>

            {/* Password */}
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

              {/* Password Strength Indicator */}
              {passwordValue && (
                <div className="mt-2 space-y-1">
                  <div className="flex items-center justify-between text-[10px]">
                    <span className="text-slate-400">Strength:</span>
                    <span className="font-semibold text-slate-300">{strength.label}</span>
                  </div>
                  <div className="w-full h-1.5 bg-slate-800 rounded-full overflow-hidden">
                    <div
                      className={`h-full transition-all duration-300 ${strength.color}`}
                      style={{ width: `${strength.score}%` }}
                    />
                  </div>
                </div>
              )}

              {errors.password && (
                <p className="mt-1 text-[11px] text-rose-400">{errors.password.message}</p>
              )}
            </div>

            {/* Confirm Password */}
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1.5">
                Confirm Password
              </label>
              <div className="relative">
                <Lock className="absolute left-3.5 top-3 w-4 h-4 text-slate-500" />
                <input
                  type="password"
                  {...register('confirmPassword')}
                  placeholder="••••••••••••"
                  className={`w-full pl-10 pr-3.5 py-2.5 text-xs bg-slate-800/90 border rounded-xl text-white placeholder-slate-500 focus:outline-none transition-colors ${
                    errors.confirmPassword
                      ? 'border-rose-500 focus:border-rose-500'
                      : 'border-slate-700 focus:border-indigo-500'
                  }`}
                />
              </div>
              {errors.confirmPassword && (
                <p className="mt-1 text-[11px] text-rose-400">{errors.confirmPassword.message}</p>
              )}
            </div>

            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full mt-2 flex items-center justify-center space-x-2 py-3 px-4 rounded-xl text-xs font-bold text-white bg-indigo-600 hover:bg-indigo-500 shadow-lg shadow-indigo-600/30 transition-all disabled:opacity-50"
            >
              <span>{isSubmitting ? 'Creating account...' : 'Create Account'}</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </form>

          <div className="mt-6 text-center text-xs text-slate-400">
            Already have an account?{' '}
            <Link to="/login" className="text-indigo-400 hover:text-indigo-300 font-semibold">
              Sign in
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};
