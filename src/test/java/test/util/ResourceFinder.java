package test.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResourceFinder implements Iterable<String> {

    private List<String> resources;
    private Pattern pattern;

    public ResourceFinder(String regexp) throws IOException {
        resources = new ArrayList<String>();
        pattern = Pattern.compile(regexp);
        find();
    }

    private void find() throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl instanceof URLClassLoader == false)
            return;
        URL[] urls = ((URLClassLoader) cl).getURLs();
        if (urls.length == 1 && urls[0].getFile().contains("surefirebooter")) {
            urls = readURLsFromManifest(urls[0]);
        }
        for (URL u : urls) {
            findInURL(u);
        }
    }

    private URL[] readURLsFromManifest(URL jarFile) throws IOException {
        ZipFile zf = new ZipFile(jarFile.getFile());
        try {
            InputStream is = zf.getInputStream(new ZipEntry(
                    "META-INF/MANIFEST.MF"));
            Manifest mf = new Manifest(is);
            String cp = mf.getMainAttributes().getValue("Class-Path");
            String[] parts = cp.split(" ");
            URL[] urls = new URL[parts.length];
            for (int i = 0; i < parts.length; i++)
                urls[i] = new URL(parts[i]);
            return urls;
        } finally {
            zf.close();
        }
    }

    private void findInURL(URL u) throws IOException {
        File file = new File(URLDecoder.decode(u.getFile(), "utf-8"));
        if (file.isDirectory()) {
            resources.addAll(new DirectoryFinder(file).find());
        } else if (file.getName().toLowerCase().endsWith(".jar")) {
            resources.addAll(new JarFinder(file).find());
        } else {
            throw new UnsupportedOperationException("Unknown URL: " + u);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return resources.iterator();
    }

    private class JarFinder {
        private File file;

        JarFinder(File file) {
            this.file = file;
        }

        private Collection<String> find() throws IOException {
            List<String> list = new ArrayList<String>();
            ZipFile f = new ZipFile(file);
            try {
                Enumeration<? extends ZipEntry> entries = f.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (pattern.matcher(entry.getName()).matches()) {
                        list.add(entry.getName());
                    }
                }
            } finally {
                f.close();
            }
            return list;
        }
    }

    private class DirectoryFinder {
        private File dir;

        DirectoryFinder(File dir) {
            this.dir = dir;
        }

        private Collection<String> find() {
            List<String> list = new ArrayList<String>();
            visit(dir, list);
            return list;
        }

        private void visit(File f, List<String> list) {
            String relative = dir.toURI().relativize(f.toURI()).getPath();
            if (f.isFile() && pattern.matcher(relative).matches()) {
                list.add(relative);
            }
            if (f.isDirectory()) {
                for (File ff : f.listFiles()) {
                    visit(ff, list);
                }
            }
        }
    }
}
