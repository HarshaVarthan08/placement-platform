import React from 'react';
import { Controller, useFormContext } from 'react-hook-form';
import { TextField } from '@mui/material';
import type { TextFieldProps } from '@mui/material';

interface FormTextFieldProps extends Omit<TextFieldProps, 'name'> {
  name: string;
  InputProps?: Record<string, unknown>;
}

export const FormTextField: React.FC<FormTextFieldProps> = ({ name, InputProps, ...rest }) => {
  const { control } = useFormContext();

  return (
    <Controller
      name={name}
      control={control}
      render={({ field, fieldState: { error } }) => (
        <TextField
          {...field}
          {...rest}
          value={field.value ?? ''}
          error={!!error}
          helperText={error ? error.message : rest.helperText}
          fullWidth
          variant="outlined"
          slotProps={{
            input: InputProps,
          }}
          sx={{
            mb: 2.5,
            ...rest.sx,
          }}
        />
      )}
    />
  );
};

export default FormTextField;
