import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Tooltip,
  Box,
  Typography,
} from '@mui/material';
import type { NavigationItem } from '../../../config/navigation';

interface SidebarLinkProps {
  item: NavigationItem;
  isActive: boolean;
  collapsed: boolean;
  onClick?: () => void;
}

export const SidebarLink: React.FC<SidebarLinkProps> = ({ item, isActive, collapsed, onClick }) => {
  const { label, icon: Icon, route } = item;

  const isAction = route === '/logout';

  const buttonContent = (
    <ListItemButton
      {...(!isAction ? { component: NavLink, to: route } : {})}
      onClick={onClick}
      aria-current={isActive ? 'page' : undefined}
      sx={{
        minHeight: 48,
        px: collapsed ? 2.5 : 3,
        justifyContent: collapsed ? 'center' : 'flex-start',
        borderRadius: 1.5,
        mx: 1,
        mb: 0.5,
        backgroundColor: isActive ? 'rgba(0, 102, 255, 0.08)' : 'transparent',
        color: isActive ? 'primary.main' : 'text.secondary',
        '&:hover': {
          backgroundColor: isActive ? 'rgba(0, 102, 255, 0.12)' : 'action.hover',
          color: isActive ? 'primary.main' : 'text.primary',
        },
        transition: 'all 0.2s ease',
        position: 'relative',
      }}
    >
      {/* Active Indicator Line */}
      {isActive && (
        <Box
          sx={{
            position: 'absolute',
            left: 0,
            top: '25%',
            height: '50%',
            width: 4,
            backgroundColor: 'primary.main',
            borderRadius: '0 4px 4px 0',
          }}
        />
      )}

      <ListItemIcon
        sx={{
          minWidth: 0,
          mr: collapsed ? 'auto' : 2,
          justifyContent: 'center',
          color: isActive ? 'primary.main' : 'text.secondary',
          transition: 'margin 0.2s ease',
        }}
      >
        <Icon size={20} />
      </ListItemIcon>

      {!collapsed && (
        <ListItemText
          primary={
            <Typography
              variant="body2"
              sx={{
                fontWeight: isActive ? 600 : 500,
                fontSize: '0.875rem',
              }}
            >
              {label}
            </Typography>
          }
        />
      )}
    </ListItemButton>
  );

  if (collapsed) {
    return (
      <Tooltip title={label} placement="right" arrow enterDelay={100}>
        <ListItem disablePadding sx={{ display: 'block' }}>
          {buttonContent}
        </ListItem>
      </Tooltip>
    );
  }

  return (
    <ListItem disablePadding sx={{ display: 'block' }}>
      {buttonContent}
    </ListItem>
  );
};

export default SidebarLink;
