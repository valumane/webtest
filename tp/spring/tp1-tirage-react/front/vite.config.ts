import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { fileURLToPath, URL } from 'node:url';

const rootDir = fileURLToPath(new URL('.', import.meta.url));

// Construit une application multi-page (une entrée par écran), comme le faisait
// Parcel avec login.html et livredor.html. Le résultat est déposé directement
// dans target/classes/public, servi par Spring Boot.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
      '/login': 'http://localhost:8080',
    },
  },
  build: {
    outDir: '../target/classes/static',
    emptyOutDir: true,
    rollupOptions: {
      input: {
        index: `${rootDir}index.html`,
        tirage: `${rootDir}tirages.html`,
        admin: `${rootDir}admin.html`,
      },
    },
  },
});
