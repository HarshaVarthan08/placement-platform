import { useState, useEffect } from 'react';

/**
 * Custom hook to detect if the page has been scrolled past a certain threshold.
 * Optimized with passive scroll listener and state update checks.
 *
 * @param threshold The scroll threshold in pixels (default: 0)
 * @returns boolean indicating if window.scrollY is greater than the threshold
 */
export const useScrolled = (threshold: number = 0): boolean => {
  const [scrolled, setScrolled] = useState(() => {
    return typeof window !== 'undefined' ? window.scrollY > threshold : false;
  });

  useEffect(() => {
    const handleScroll = () => {
      const isOverThreshold = window.scrollY > threshold;
      setScrolled((prev) => {
        if (prev !== isOverThreshold) {
          return isOverThreshold;
        }
        return prev;
      });
    };

    // Use passive event listener for better scroll performance
    window.addEventListener('scroll', handleScroll, { passive: true });
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [threshold]);

  return scrolled;
};

export default useScrolled;
