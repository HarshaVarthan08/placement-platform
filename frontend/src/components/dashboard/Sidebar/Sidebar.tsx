import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import {
  Box,
  Divider,
  IconButton,
  List,
  Typography,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
  ListSubheader,
  Tooltip,
} from '@mui/material';
import { Orbit, ChevronLeft, ChevronRight, X } from 'lucide-react';
import { useUI } from '../../../hooks/useUI';
import { useAuth } from '../../../hooks/useAuth';
import { NAVIGATION_GROUPS, LOGOUT_ITEM } from '../../../config/navigation';
import SidebarLink from './SidebarLink';

interface SidebarProps {
  isMobileDrawer?: boolean;
}

export const Sidebar: React.FC<SidebarProps> = ({ isMobileDrawer = false }) => {
  const location = useLocation();
  const navigate = useNavigate();
  const { sidebarOpen, toggleSidebar, closeMobileSidebar } = useUI();
  const { logout } = useAuth();
  const [logoutDialogOpen, setLogoutDialogOpen] = useState<boolean>(false);

  // In mobile drawer mode, we always render the sidebar in its expanded state
  const isCollapsed = !isMobileDrawer && !sidebarOpen;

  const handleLinkClick = () => {
    if (isMobileDrawer) {
      closeMobileSidebar();
    }
  };

  const handleLogoutClick = () => {
    setLogoutDialogOpen(true);
  };

  const handleConfirmLogout = () => {
    setLogoutDialogOpen(false);
    if (isMobileDrawer) {
      closeMobileSidebar();
    }
    logout();
    navigate('/login');
  };

  const handleCloseLogoutDialog = () => {
    setLogoutDialogOpen(false);
  };

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        height: '100%',
        width: '100%',
        backgroundColor: 'background.paper',
        borderRight: '1px solid',
        borderColor: 'divider',
        userSelect: 'none',
      }}
    >
      {/* Brand Header */}
      <Box
        sx={{
          height: 64,
          minHeight: 64,
          display: 'flex',
          alignItems: 'center',
          justifyContent: isCollapsed ? 'center' : 'space-between',
          px: isCollapsed ? 2 : 3,
          borderBottom: '1px solid',
          borderColor: 'divider',
        }}
      >
        <Box
          onClick={() => {
            navigate('/dashboard');
            handleLinkClick();
          }}
          sx={{
            display: 'flex',
            alignItems: 'center',
            gap: 1.5,
            cursor: 'pointer',
            overflow: 'hidden',
          }}
        >
          <Box sx={{ color: 'primary.main', display: 'flex', alignItems: 'center' }}>
            <Orbit size={28} strokeWidth={2.5} />
          </Box>
          {!isCollapsed && (
            <Typography
              variant="h6"
              sx={{
                fontWeight: 'bold',
                fontSize: '1rem',
                letterSpacing: '-0.02em',
                whiteSpace: 'nowrap',
                color: 'text.primary',
              }}
            >
              Placement Platform
            </Typography>
          )}
        </Box>

        {/* Toggle/Close Button */}
        {isMobileDrawer ? (
          <IconButton onClick={closeMobileSidebar} aria-label="Close menu drawer" size="small">
            <X size={20} />
          </IconButton>
        ) : (
          !isCollapsed && (
            <IconButton onClick={toggleSidebar} aria-label="Collapse sidebar" size="small">
              <ChevronLeft size={20} />
            </IconButton>
          )
        )}
      </Box>

      {/* Navigation Groups */}
      <Box
        sx={{
          flexGrow: 1,
          overflowY: 'auto',
          overflowX: 'hidden',
          py: 2,
          '&::-webkit-scrollbar': {
            width: '4px',
          },
          '&::-webkit-scrollbar-thumb': {
            backgroundColor: 'divider',
            borderRadius: '4px',
          },
        }}
      >
        {NAVIGATION_GROUPS.map((group, index) => (
          <List
            key={group.groupLabel}
            subheader={
              !isCollapsed ? (
                <ListSubheader
                  sx={{
                    lineHeight: '24px',
                    fontSize: '0.75rem',
                    fontWeight: 700,
                    textTransform: 'uppercase',
                    color: 'text.disabled',
                    backgroundColor: 'transparent',
                    pl: 3,
                    mb: 0.5,
                    mt: index > 0 ? 2 : 0,
                  }}
                >
                  {group.groupLabel}
                </ListSubheader>
              ) : undefined
            }
            disablePadding
          >
            {group.items.map((item) => (
              <SidebarLink
                key={item.label}
                item={item}
                isActive={location.pathname === item.route}
                collapsed={isCollapsed}
                onClick={handleLinkClick}
              />
            ))}
          </List>
        ))}
      </Box>

      {/* Sidebar Footer */}
      <Box sx={{ mt: 'auto' }}>
        <Divider />
        <List disablePadding>
          <SidebarLink
            item={LOGOUT_ITEM}
            isActive={false}
            collapsed={isCollapsed}
            onClick={handleLogoutClick}
          />
        </List>

        {/* Bottom Expand Toggle (Shown only on desktop when collapsed) */}
        {!isMobileDrawer && isCollapsed ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', py: 1.5 }}>
            <Tooltip title="Expand Sidebar" placement="right">
              <IconButton onClick={toggleSidebar} aria-label="Expand sidebar" size="small">
                <ChevronRight size={20} />
              </IconButton>
            </Tooltip>
          </Box>
        ) : (
          !isCollapsed && (
            <Box sx={{ px: 3, py: 2 }}>
              <Typography
                variant="caption"
                color="text.disabled"
                sx={{ display: 'block', textAlign: 'center' }}
              >
                v1.0.0 &copy; 2026
              </Typography>
            </Box>
          )
        )}
      </Box>

      {/* Logout Confirmation Dialog */}
      <Dialog
        open={logoutDialogOpen}
        onClose={handleCloseLogoutDialog}
        aria-labelledby="logout-dialog-title"
        aria-describedby="logout-dialog-description"
      >
        <DialogTitle id="logout-dialog-title" sx={{ fontWeight: 600 }}>
          Confirm Logout
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="logout-dialog-description">
            Are you sure you want to log out of your session? You will need to sign back in to
            access the platform.
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{ px: 3, pb: 2.5 }}>
          <Button onClick={handleCloseLogoutDialog} color="inherit">
            Cancel
          </Button>
          <Button onClick={handleConfirmLogout} color="error" variant="contained" autoFocus>
            Logout
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default Sidebar;
