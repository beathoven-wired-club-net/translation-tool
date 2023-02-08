package net.wiredclub.translation;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A helper class for file operations, e.g.
 * it reads a text file, writes back the output, or scans the language
 * directory for existing languages.
 * For a GIT repo, it traverses back one commit and returns the file
 * in its previous version.
 */
public class FileHelper {

	// private static final Logger LOG = LoggerFactory.getLogger(FileHelper.class);

	public String readFile(String fileName) throws TranslationFileNotFoundException {
		try {
			return Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new TranslationFileNotFoundException(fileName);
		}
	}

	public void writeFile(String fileName, String content) throws IOException {
		Files.writeString(Paths.get(new File(fileName).toURI()),
				content,
				StandardCharsets.UTF_8,
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
	}

	public String readPreviousFileFromHistory(String repositoryPath, String fileName, int revRange)
			throws IOException, TranslationFileNotFoundException {
		try (Git git = Git.open(new File(repositoryPath))) {
			Repository repository = git.getRepository();

			Iterable<RevCommit> revCommits = git.log().setSkip(revRange).call();
			for (RevCommit revCommit : revCommits) {
				try (TreeWalk treeWalk = new TreeWalk(repository)) {
					treeWalk.addTree(revCommit.getTree());
					treeWalk.setRecursive(true);
					treeWalk.setFilter(PathFilter.create(fileName));

					if (!treeWalk.next()) {
						throw new TranslationFileNotFoundException(fileName);
					}
					ObjectId objectId = treeWalk.getObjectId(0);

					ObjectLoader loader = repository.open(objectId);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					loader.copyTo(stream);

					return stream.toString(StandardCharsets.UTF_8);
				}
			}
		} catch (GitAPIException e) {
			throw new RuntimeException(e);
		}

		throw new TranslationFileNotFoundException(fileName);
	}

	public Set<String> discoverLanguageDirectories(String dir) throws IOException {
		try (Stream<Path> stream = Files.list(Paths.get(dir))) {
			return stream.filter(Files::isDirectory)
					.map(Path::getFileName)
					.map(Path::toString)
					.collect(Collectors.toSet());
		}
	}
}
