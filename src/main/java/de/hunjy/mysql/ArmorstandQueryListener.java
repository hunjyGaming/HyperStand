package de.hunjy.mysql;

import de.hunjy.template.ArmorStandTemplate;

import java.util.List;

public interface ArmorstandQueryListener {
    void onQueryResult(List<ArmorStandTemplate> templates);
    void onQueryError(Exception exception);
}
