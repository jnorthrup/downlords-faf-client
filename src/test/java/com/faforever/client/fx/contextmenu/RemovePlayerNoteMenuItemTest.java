package com.faforever.client.fx.contextmenu;

import com.faforever.client.builders.PlayerBeanBuilder;
import com.faforever.client.domain.PlayerBean;
import com.faforever.client.i18n.I18n;
import com.faforever.client.player.PlayerService;
import com.faforever.client.test.UITest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

public class RemovePlayerNoteMenuItemTest extends UITest {

  @Mock
  private PlayerService playerService;
  @Mock
  private I18n i18n;

  @InjectMocks
  RemovePlayerNoteMenuItem instance;

  @Test
  public void testOnItemClicked() {
    PlayerBean player = PlayerBeanBuilder.create().defaultValues().note("junit").get();
    instance.setObject(player);
    instance.onClicked();
    verify(playerService).removeNote(player);
  }

  @Test
  public void testVisibleItem() {
    instance.setObject(PlayerBeanBuilder.create().defaultValues().note("junit").get());
    assertTrue(instance.isVisible());
  }

  @Test
  public void testInvisibleItemIfNoteIsBlank() {
    instance.setObject(PlayerBeanBuilder.create().defaultValues().get());
    assertFalse(instance.isVisible());
  }

  @Test
  public void testInvisibleItemIfNoPlayer() {
    instance.setObject(null);
    assertFalse(instance.isVisible());
  }
}