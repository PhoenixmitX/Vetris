import { defineConfig } from "vite";
import scalaJSPlugin from "@scala-js/vite-plugin-scalajs";

export default defineConfig({
  root: "modules/app/src/main/vite",
  publicDir: "../public",
  resolve: {
    alias: {
      // fix signal-polyfill import
      "signal-polyfill/dist/wrapper": "signal-polyfill/dist/index.js",
    },
  },
  build: {
    outDir: "../../../dist/",
    emptyOutDir: true,
    rollupOptions: {
      output: {
        entryFileNames: "assets/[name]-[hash].js",
        assetFileNames: "assets/[name]-[hash][extname]",
      },
      treeshake: {
        preset: "smallest",
        moduleSideEffects: "no-external",
      },
    },
		watch: {
			include: ["modules/app/target/scala-3.4.2/vetris-app-fastopt/**/*"],
		}
  },
  plugins: [
    scalaJSPlugin({
      projectID: "app",
    })
  ],
});
