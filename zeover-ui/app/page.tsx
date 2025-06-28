'use client'; 
import api from '@/lib/axios';
import { useEffect, useState, useRef } from 'react';

export default function Home() {
  const [message, setMessage] = useState('Loading...');
  const hasFetched = useRef(false);

  useEffect(() => {
    if (hasFetched.current) return;
    hasFetched.current = true;

    const fetchMessage = async () => {
      try {
        const res = await api.get('/welcome');
        const greeting = res.data?.data?.greeting;
        setMessage(greeting || 'No greeting found');
      } catch (err) {
        console.error('Fetch error:', err);
        setMessage('Failed to fetch message');
      }
    };

    fetchMessage();
  }, []);

  return (
    <main>
      <h1>Frontend</h1>
      <p>Backend says: {message}</p>
    </main>
  );
}
