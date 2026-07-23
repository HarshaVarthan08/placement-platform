import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Avatar,
  Menu,
  MenuItem,
  Typography,
  Divider,
  ButtonBase,
  ListItemIcon,
} from '@mui/material';
import { User, Settings, LogOut } from 'lucide-react';
import { useAuth } from '../../../hooks/useAuth';

export const UserDropdown: React.FC = () => {
  const navigate = useNavigate();
  const { currentUser, logout } = useAuth();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);

  const name = currentUser?.name || 'Anonymous User';
  const email = currentUser?.email || '';
  const role = currentUser?.role || 'STUDENT';

  // Helper to extract user initials
  const getInitials = (userName: string) => {
    const parts = userName.trim().split(/\s+/);
    if (parts.length === 0 || !parts[0]) return 'U';
    if (parts.length === 1) return parts[0].charAt(0).toUpperCase();
    return (parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
  };

  const initials = getInitials(name);

  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleNavigate = (path: string) => {
    handleClose();
    navigate(path);
  };

  const handleLogout = () => {
    handleClose();
    logout();
    navigate('/login');
  };

  return (
    <Box>
      <ButtonBase
        onClick={handleClick}
        aria-haspopup="true"
        aria-expanded={open ? 'true' : undefined}
        aria-label="User account menu"
        sx={{
          p: 0.5,
          borderRadius: 2,
          display: 'flex',
          alignItems: 'center',
          gap: 1.5,
          '&:hover': {
            backgroundColor: 'action.hover',
          },
          transition: 'background-color 0.2s ease',
          textAlign: 'left',
        }}
      >
        {/* User Info (Name & Role) - Hidden on Mobile */}
        <Box
          sx={{
            display: { xs: 'none', sm: 'flex' },
            flexDirection: 'column',
            alignItems: 'flex-end',
          }}
        >
          <Typography
            variant="body2"
            sx={{
              fontWeight: 600,
              color: 'text.primary',
              fontSize: '0.875rem',
              lineHeight: 1.2,
            }}
          >
            {name}
          </Typography>
          <Typography
            variant="caption"
            sx={{
              color: 'text.secondary',
              fontWeight: 500,
              fontSize: '0.75rem',
              textTransform: 'capitalize',
            }}
          >
            {role.toLowerCase()}
          </Typography>
        </Box>

        {/* Avatar Initials Bubble */}
        <Avatar
          sx={{
            width: 36,
            height: 36,
            fontSize: '0.875rem',
            fontWeight: 700,
            backgroundColor: 'primary.main',
            color: 'primary.contrastText',
            boxShadow: 1,
          }}
        >
          {initials}
        </Avatar>
      </ButtonBase>

      {/* User Account Popover Menu */}
      <Menu
        anchorEl={anchorEl}
        id="user-account-menu"
        open={open}
        onClose={handleClose}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
        slotProps={{
          paper: {
            elevation: 3,
            sx: {
              mt: 1.5,
              minWidth: 200,
              borderRadius: 2,
              border: '1px solid',
              borderColor: 'divider',
              overflow: 'visible',
              '&::before': {
                content: '""',
                display: 'block',
                position: 'absolute',
                top: 0,
                right: 18,
                width: 10,
                height: 10,
                bgcolor: 'background.paper',
                transform: 'translateY(-50%) rotate(45deg)',
                zIndex: 0,
                borderLeft: '1px solid',
                borderTop: '1px solid',
                borderColor: 'divider',
              },
            },
          },
        }}
      >
        {/* Menu Header with User Info */}
        <Box sx={{ px: 2, py: 1.5 }}>
          <Typography variant="subtitle2" sx={{ fontWeight: 600, color: 'text.primary' }}>
            {name}
          </Typography>
          {email && (
            <Typography variant="caption" sx={{ color: 'text.secondary', display: 'block' }}>
              {email}
            </Typography>
          )}
        </Box>
        <Divider />

        <MenuItem onClick={() => handleNavigate('/profile')} sx={{ py: 1.2 }}>
          <ListItemIcon sx={{ color: 'text.secondary' }}>
            <User size={18} />
          </ListItemIcon>
          <Typography variant="body2" sx={{ fontWeight: 500 }}>
            Profile
          </Typography>
        </MenuItem>

        <MenuItem onClick={() => handleNavigate('/settings')} sx={{ py: 1.2 }}>
          <ListItemIcon sx={{ color: 'text.secondary' }}>
            <Settings size={18} />
          </ListItemIcon>
          <Typography variant="body2" sx={{ fontWeight: 500 }}>
            Settings
          </Typography>
        </MenuItem>

        <Divider />

        <MenuItem onClick={handleLogout} sx={{ py: 1.2, color: 'error.main' }}>
          <ListItemIcon sx={{ color: 'error.main' }}>
            <LogOut size={18} />
          </ListItemIcon>
          <Typography variant="body2" sx={{ fontWeight: 600 }}>
            Logout
          </Typography>
        </MenuItem>
      </Menu>
    </Box>
  );
};

export default UserDropdown;
