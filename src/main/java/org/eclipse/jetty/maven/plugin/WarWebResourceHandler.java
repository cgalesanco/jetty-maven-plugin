package org.eclipse.jetty.maven.plugin;

import org.apache.maven.model.Resource;
import org.codehaus.plexus.util.DirectoryScanner;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WarWebResourceHandler extends ContextHandler
{
    public WarWebResourceHandler(Resource resource) throws IOException
    {
        final File baseDirectory = new File(resource.getDirectory());
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(resource.getDirectory());
        if ( resource.getIncludes() != null && !resource.getIncludes().isEmpty() )
        {
            scanner.setIncludes(
                    (String[])resource.getIncludes().toArray(new String[resource.getIncludes().size()]));
        }
        else
        {
            scanner.setIncludes(DEFALT_INCLUDES);
        }

        if (resource.getExcludes() != null && !resource.getExcludes().isEmpty())
        {
            scanner.setExcludes(
                    (String[])resource.getExcludes().toArray(new String[resource.getExcludes().size()]));
        }
        scanner.addDefaultExcludes();

        scanner.scan();
        excludedFiles = Arrays.asList(scanner.getExcludedFiles());

        this.setContextPath("/" + resource.getTargetPath());
        ResourceHandler resHandler = new ResourceHandler();
        resHandler.setBaseResource(org.eclipse.jetty.util.resource.Resource.newResource(baseDirectory));
        this.setHandler(resHandler);
    }

    public void doHandle(final String target, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        String targetPath = target;
        if ( targetPath.startsWith("/") )
        {
            targetPath = target.substring(1);
        }
        if ( !excludedFiles.contains(targetPath) )
            super.doHandle(target, baseRequest, request, response);
    }

    private static final String[] DEFALT_INCLUDES = {"**/**"};
    private List excludedFiles;
}
