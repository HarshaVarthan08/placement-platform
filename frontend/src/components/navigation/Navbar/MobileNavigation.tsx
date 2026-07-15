import { IconButton } from '@mui/material';
import { Menu } from 'lucide-react';

export interface MobileNavigationProps {
  onOpen: () => void;
  open: boolean;
}

export const MobileNavigation = ({ onOpen, open }: MobileNavigationProps) => {
  return (
    <IconButton
      onClick={onOpen}
      edge="start"
      color="inherit"
      aria-label="Open navigation menu"
      aria-haspopup="true"
      aria-expanded={open}
      sx={{
        display: { xs: 'flex', md: 'none' }, // Hidden on laptop/desktop
        color: 'text.primary',
        padding: 1,
        '&:hover': {
          backgroundColor: 'action.hover',
        },
        '&:focus-visible': {
          outline: '2px solid',
          outlineColor: 'primary.main',
          outlineOffset: '2px',
        },
      }}
    >
      <Menu size={24} />
    </IconButton>
  );
};

export default MobileNavigation;
