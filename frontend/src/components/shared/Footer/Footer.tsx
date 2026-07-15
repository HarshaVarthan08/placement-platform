import { Box, Container, Grid } from '@mui/material';
import { FOOTER_SECTIONS } from '../../../constants';
import FooterColumn from './FooterColumn';
import FooterBottom from './FooterBottom';

export const Footer = () => {
  return (
    <Box
      component="footer"
      sx={{
        backgroundColor: 'background.paper',
        borderTop: '1px solid',
        borderColor: 'divider',
        py: { xs: 6, sm: 8 },
        width: '100%',
        boxSizing: 'border-box',
      }}
    >
      <Container
        maxWidth="lg"
        sx={{
          px: { xs: 2, sm: 3, md: 4 },
        }}
      >
        <Grid container spacing={{ xs: 4, sm: 6, md: 4 }}>
          {FOOTER_SECTIONS.map((section) => (
            <Grid size={{ xs: 12, sm: 6, md: 3 }} key={section.title}>
              <FooterColumn title={section.title} links={section.links} />
            </Grid>
          ))}
        </Grid>

        <FooterBottom />
      </Container>
    </Box>
  );
};

export default Footer;
